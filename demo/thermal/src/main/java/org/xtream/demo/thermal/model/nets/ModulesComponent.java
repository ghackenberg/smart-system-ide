package org.xtream.demo.thermal.model.nets;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.SolarComponent;
import org.xtream.demo.thermal.model.StorageComponent;
import org.xtream.demo.thermal.model.ThermalComponent;

public class ModulesComponent extends AbstractModulesComponent
{
	
	@SuppressWarnings("unchecked")
	public ModulesComponent(int size)
	{
		// Solar
		
		solar = new SolarComponent(size * 400.);
		
		// Storage
		
		storage = new StorageComponent(size * 200., size * 4000.);
		
		// Thermals
		
		thermals = new ThermalComponent[size];
		
		for (int i = 0; i < thermals.length; i++)
		{
			thermals[i] = new ThermalComponent();
		}
		
		// Balance outputs
		
		balanceOutputs = new Port[size + 2];
		
		for (int i = 0; i < size + 2; i++)
		{
			balanceOutputs[i] = new Port<>();
		}
		
		// Balance channels
		
		balances = new ChannelExpression[size + 2];
		balances[0] = new ChannelExpression<>(balanceOutputs[0], storage.balanceOutput);
		balances[1] = new ChannelExpression<>(balanceOutputs[1], solar.balanceOutput);
		
		for (int i = 0; i < thermals.length; i++)
		{
			balances[i + 2] = new ChannelExpression<>(balanceOutputs[i + 2], thermals[i].balanceOutput);
		}
		
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
	// Components

	public SolarComponent solar;
	public StorageComponent storage;
	public ThermalComponent[] thermals;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;

}
