package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.datatypes.Edge;
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
		positionTraversedLengthOutputs = new Port[modules.length];
		positionOutputs = new Port[modules.length];
		vehicleLengthOutputs = new Port[modules.length];
		
		
		for (int i = 0; i < modules.length; i++)
		{
			balanceOutputs[i] = new Port<>();
			/*
			positionTraversedLengthOutputs[i] = new Port<>();
			positionOutputs[i] = new Port<>();
			vehicleLengthOutputs[i] = new Port<>();
			*/
		}
		
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		
		positionTraversedLength = new ChannelExpression[modules.length];
		position = new ChannelExpression[modules.length];
		vehicleLength = new ChannelExpression[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			balances[i] = new ChannelExpression<>(balanceOutputs[i], modules[i].balanceOutput);	

			//positionTraversedLength[i] = new ChannelExpression<>(positionTraversedLengthOutputs[i], modules[i].positionTraversedLengthOutput);
			//position[i] = new ChannelExpression<>(positionOutputs[i], modules[i].positionOutput);
			//vehicleLength[i] = new ChannelExpression<>(vehicleLengthOutputs[i], modules[i].vehicleLengthOutput);
			
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
	
	public Port<Double>[] positionTraversedLengthOutputs;
	public Port<Edge>[] positionOutputs;
	public Port<Double>[] vehicleLengthOutputs;
	
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;
	
	// Charts
	
	public Chart balanceCharts[];
	
}
