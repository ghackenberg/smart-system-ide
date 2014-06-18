package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.components.AbstractModulesComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.State;
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
		powerOutputs = new Port[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			balanceOutputs[i] = new Port<>();
			
			positionTraversedLengthOutputs[i] = new Port<>();
			positionOutputs[i] = new Port<>();
			vehicleLengthOutputs[i] = new Port<>();
			
			timeCostOutputs[i] = new Port<>();
			powerCostOutputs[i] = new Port<>();
			powerOutputs[i] = new Port<>();
			
		}
		
		// Balance channels
		
		balances = new ChannelExpression[modules.length];
		
		positionTraversedLength = new ChannelExpression[modules.length];
		position = new ChannelExpression[modules.length];
		vehicleLength = new ChannelExpression[modules.length];
		
		timeCost = new ChannelExpression[modules.length];
		powerCost = new ChannelExpression[modules.length];
		
		power = new ChannelExpression[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			VehicleComponent vehicleModule = (VehicleComponent) modules[i];
			balances[i] = new ChannelExpression<>(balanceOutputs[i], vehicleModule.balanceOutput);	
			positionTraversedLength[i] = new ChannelExpression<>(positionTraversedLengthOutputs[i], vehicleModule.positionTraversedLengthOutput);
			position[i] = new ChannelExpression<>(positionOutputs[i], vehicleModule.positionOutput);
			vehicleLength[i] = new ChannelExpression<>(vehicleLengthOutputs[i], vehicleModule.vehicleLengthOutput);
			timeCost[i] = new ChannelExpression<>(timeCostOutputs[i], vehicleModule.timeCostOutput);
			powerCost[i] = new ChannelExpression<>(powerCostOutputs[i], vehicleModule.powerCostOutput);
			power[i] = new ChannelExpression<>(powerOutputs[i], vehicleModule.powerOutput);

		}
		
		// Balance charts
		
		//balanceCharts = new Chart[modules.length];
		//timeCostCharts = new Chart[modules.length];
		//powerCostCharts = new Chart[modules.length];
		
		for (int i = 0; i < modules.length; i++)
		{
			//balanceCharts[i] = new Chart(balanceOutputs[i]);
			//timeCostCharts[i] = new Chart(timeCostOutputs[i]);
			//powerCostCharts[i] = new Chart(powerCostOutputs[i]);
		}
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
	public Port<Double>[] positionTraversedLengthOutputs;
	public Port<Edge>[] positionOutputs;
	public Port<Double>[] vehicleLengthOutputs;
	
	public Port<Double>[] timeCostOutputs;
	public Port<Double>[] powerCostOutputs;
	public Port<Double>[] powerOutputs;
	public Port<Double> powerOutput = new Port<>();
	public Port<Double> powerAggregateOutput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;
	
	public ChannelExpression<Double>[] timeCost;
	public ChannelExpression<Double>[] powerCost;
	
	public ChannelExpression<Double>[] power;
	
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < modules.length; i++)
			{
				sum += powerOutputs[i].get(state, timepoint);
			}
			
			return sum;
		}
	};
	
	public Expression<Double> powerAggregateExpression = new Expression<Double>(powerAggregateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : powerAggregateOutput.get(state, timepoint - 1)) + powerOutput.get(state, timepoint);
		}
	};
	
	// Charts
	
	//public Chart balanceCharts[];
	
	//public Chart timeCostCharts[];
	//public Chart powerCostCharts[];
	
	public Chart PowerChart = new Timeline(powerOutput);
	public Chart powerPlaceholderChart = new Timeline(powerOutput);
	public Chart powerAggregateChart = new Timeline(powerAggregateOutput);
}
