package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.commons.EnergyModuleComponent;
import org.xtream.demo.thermal.model.nets.ConstraintsComponent;
import org.xtream.demo.thermal.model.nets.CostsComponent;
import org.xtream.demo.thermal.model.nets.LogicsComponent;
import org.xtream.demo.thermal.model.nets.ModulesComponent;
import org.xtream.demo.thermal.model.nets.PhysicsComponent;
import org.xtream.demo.thermal.model.nets.QualitiesComponent;

public abstract class NetComponent<ConcreteModulesComponent extends ModulesComponent> extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ConcreteModulesComponent>
{
	
	@SuppressWarnings("unchecked")
	public NetComponent(int size, ConcreteModulesComponent modules)
	{
		super(new PhysicsComponent(size), new LogicsComponent(), new ConstraintsComponent(size * 200.), new QualitiesComponent(), new CostsComponent(), modules);
		
		// Balance channels
		
		balances = new ChannelExpression[size];
		
		for (int i = 0; i < size; i++)
		{
			balances[i] = new ChannelExpression<>(physics.terminalInputs[i], modules.balanceOutputs[i]);
		}
	}
	
	// Channels
	
	public ChannelExpression<Double> productionInternal = new ChannelExpression<>(constraints.productionInput, physics.productionOutput);
	public ChannelExpression<Double> consumptionInternal = new ChannelExpression<>(constraints.consumptionInput, physics.consumptionOutput);
	public ChannelExpression<Double>[] balances;
	
	// Previews
	
	public Chart energyPreview = new Chart(productionOutput, consumptionOutput, balanceOutput);

}
