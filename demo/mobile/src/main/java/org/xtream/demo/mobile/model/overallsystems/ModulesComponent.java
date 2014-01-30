package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.commons.EnergyModuleComponent;

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
		
		// Balance charts
		
		balanceCharts = new Chart[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			balanceCharts[i] = new Chart(balanceOutputs[i]);
		}
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
	// Charts
	
	public Chart balanceCharts[];
	
}
