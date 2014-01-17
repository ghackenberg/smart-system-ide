package org.xtream.demo.thermal.model.nets;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.commons.EnergyModuleComponent;

public class ModulesComponent extends AbstractModulesComponent
{
	
	@SuppressWarnings("unchecked")
	public ModulesComponent(EnergyModuleComponent<?, ?, ?, ?, ?, ?>... modules)
	{
		super(modules);
		
		// Balance outputs
		
		balanceOutputs = new Port[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			balanceOutputs[i] = new Port<>();
		}
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			balances[i] = new ChannelExpression<>(balanceOutputs[i], modules[i].balanceOutput);
		}
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
}
