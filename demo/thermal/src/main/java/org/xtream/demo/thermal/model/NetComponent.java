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

public class NetComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{
	
	@SuppressWarnings("unchecked")
	public NetComponent(double capacity, EnergyModuleComponent<?, ?, ?, ?, ?, ?>... modules)
	{
		super(NetComponent.class.getClassLoader().getResource("net.gif"), new PhysicsComponent(modules.length), new LogicsComponent(), new ConstraintsComponent(capacity), new QualitiesComponent(), new CostsComponent(), new ModulesComponent(modules));
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			balances[i] = new ChannelExpression<>(physics.terminalInputs[i], this.modules.balanceOutputs[i]);
		}
	}
	
	// Channels
	
	public ChannelExpression<Double> productionInternal = new ChannelExpression<>(constraints.productionInput, physics.productionOutput);
	public ChannelExpression<Double> consumptionInternal = new ChannelExpression<>(constraints.consumptionInput, physics.consumptionOutput);
	public ChannelExpression<Double>[] balances;
	
	// Previews
	
	public Chart energyPreview = new Chart(productionOutput, consumptionOutput, balanceOutput);

}
