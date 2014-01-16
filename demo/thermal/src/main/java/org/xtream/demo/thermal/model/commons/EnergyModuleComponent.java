package org.xtream.demo.thermal.model.commons;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractConstraintsComponent;
import org.xtream.core.model.components.AbstractCostsComponent;
import org.xtream.core.model.components.AbstractLogicsComponent;
import org.xtream.core.model.components.AbstractModuleComponent;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.components.AbstractQualitiesComponent;
import org.xtream.core.model.expressions.ChannelExpression;

public class EnergyModuleComponent<PhysicsComponent extends EnergyPhysicsComponent, LogicsComponent extends AbstractLogicsComponent, ConstraintsComponent extends AbstractConstraintsComponent, QualitiesComponent extends AbstractQualitiesComponent, CostsComponent extends AbstractCostsComponent, ModulesComponent extends AbstractModulesComponent> extends AbstractModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{
	
	public EnergyModuleComponent(PhysicsComponent physics, LogicsComponent logics, ConstraintsComponent constraints, QualitiesComponent qualities, CostsComponent costs, ModulesComponent modules)
	{
		super(physics, logics, constraints, qualities, costs, modules);
	}
	
	// Ports
	
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double> production = new ChannelExpression<>(productionOutput, physics.productionOutput);
	public ChannelExpression<Double> consumption = new ChannelExpression<>(consumptionOutput, physics.consumptionOutput);
	public ChannelExpression<Double> balance = new ChannelExpression<>(balanceOutput, physics.balanceOutput);
	
	// Charts
	
	public Chart energyChart = new Chart(productionOutput, consumptionOutput, balanceOutput);
	
}
