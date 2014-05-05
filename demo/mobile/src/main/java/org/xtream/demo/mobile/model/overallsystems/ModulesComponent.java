package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.VehicleComponent;
import org.xtream.demo.mobile.model.commons.VehicleEnergyModuleComponent;

public class ModulesComponent extends AbstractModulesComponent
{
	
	@SuppressWarnings("unchecked")
	public ModulesComponent(VehicleEnergyModuleComponent<?, ?, ?, ?, ?, ?>... modules)
	{
		super(modules);
		
		for (int i = 0; i < modules.length; i++)
		{
			modules[i] = (VehicleComponent) modules[i];
		}
		
		// Balance outputs
		
		balanceOutputs = new Port[modules.length];
		positionTraversedLengthOutputs = new Port[modules.length];
		positionOutputs = new Port[modules.length];
		vehicleLengthOutputs = new Port[modules.length];
		timeCostOutputs = new Port[modules.length];
		powerCostOutputs = new Port[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			balanceOutputs[i] = new Port<>();
			
			positionTraversedLengthOutputs[i] = new Port<>();
			positionOutputs[i] = new Port<>();
			vehicleLengthOutputs[i] = new Port<>();
			
			timeCostOutputs[i] = new Port<>();
			powerCostOutputs[i] = new Port<>();
			
		}
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		
		positionTraversedLength = new ChannelExpression[modules.length];
		position = new ChannelExpression[modules.length];
		vehicleLength = new ChannelExpression[modules.length];
		
		timeCost = new ChannelExpression[modules.length];
		powerCost = new ChannelExpression[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			VehicleComponent vehicleModule = (VehicleComponent) modules[i];
			balances[i] = new ChannelExpression<>(balanceOutputs[i], vehicleModule.balanceOutput);	
			positionTraversedLength[i] = new ChannelExpression<>(positionTraversedLengthOutputs[i], vehicleModule.positionTraversedLengthOutput);
			position[i] = new ChannelExpression<>(positionOutputs[i], vehicleModule.positionOutput);
			vehicleLength[i] = new ChannelExpression<>(vehicleLengthOutputs[i], vehicleModule.vehicleLengthOutput);
			timeCost[i] = new ChannelExpression<>(timeCostOutputs[i], vehicleModule.timeCostOutput);
			powerCost[i] = new ChannelExpression<>(powerCostOutputs[i], vehicleModule.powerCostOutput);
		}
		
		// Balance charts
		
		//balanceCharts = new Chart[modules.length];
		timeCostCharts = new Chart[modules.length];
		powerCostCharts = new Chart[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			//balanceCharts[i] = new Chart(balanceOutputs[i]);
			timeCostCharts[i] = new Chart(timeCostOutputs[i]);
			powerCostCharts[i] = new Chart(powerCostOutputs[i]);
		}
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
	public Port<Double>[] positionTraversedLengthOutputs;
	public Port<Edge>[] positionOutputs;
	public Port<Double>[] vehicleLengthOutputs;
	
	public Port<Double>[] timeCostOutputs;
	public Port<Double>[] powerCostOutputs;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;
	
	public ChannelExpression<Double>[] timeCost;
	public ChannelExpression<Double>[] powerCost;
	
	
	// Charts
	
	//public Chart balanceCharts[];
	
	public Chart timeCostCharts[];
	public Chart powerCostCharts[];
	
}
