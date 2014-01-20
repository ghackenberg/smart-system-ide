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
	
	private static EnergyModuleComponent<?, ?, ?, ?, ?, ?>[] getModules(int size)
	{
		EnergyModuleComponent<?, ?, ?, ?, ?, ?>[] modules = new EnergyModuleComponent[size + 2];
		
		modules[0] = new SolarComponent(size * 400.);
		modules[1] = new StorageComponent(size * 200., size * 1000.);
		for (int i = 0; i < size; i++)
		{
			modules[i + 2] = new ThermalComponent();
		}
		
		return modules;
	}
	
	public NetComponent(double capacity, int size)
	{
		this(capacity, getModules(size));
	}
	@SuppressWarnings("unchecked")
	public NetComponent(double capacity, EnergyModuleComponent<?, ?, ?, ?, ?, ?>... modules)
	{
		super(NetComponent.class.getClassLoader().getResource("net.png"), new PhysicsComponent(modules.length), new LogicsComponent(), new ConstraintsComponent(capacity), new QualitiesComponent(), new CostsComponent(), new ModulesComponent(modules));
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			balances[i] = new ChannelExpression<>(physics.terminalInputs[i], this.modules.balanceOutputs[i]);
		}
		
		// Previews
		
		modulePreview = new Chart(productionOutput, consumptionOutput, balanceOutput);
	}
	
	// Channels
	
	public ChannelExpression<Double> productionInternal = new ChannelExpression<>(constraints.productionInput, physics.productionOutput);
	public ChannelExpression<Double> consumptionInternal = new ChannelExpression<>(constraints.consumptionInput, physics.consumptionOutput);
	public ChannelExpression<Double>[] balances;

}
