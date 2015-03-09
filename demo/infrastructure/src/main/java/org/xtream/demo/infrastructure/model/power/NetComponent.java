package org.xtream.demo.infrastructure.model.power;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.infrastructure.model.EnergyComponent;

public class NetComponent extends EnergyComponent
{
	
	@SuppressWarnings("unchecked")
	public NetComponent(int size, double balanceWeight)
	{
		this.capacity = Double.MAX_VALUE;
		this.balanceWeight = balanceWeight;
		
		// Balance channels
		
		balanceInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			balanceInputs[i] = new Port<>();
		}
	}
	
	// Parameters
	
	public double capacity;
	public double balanceWeight;
	
	// Inputs
	
	public Port<Double>[] balanceInputs;
	
	// Outputs
	
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	public Port<Double> balanceWeightOutput = new Port<>();
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double production = 0;
			
			for (Port<Double> terminal : balanceInputs)
			{
				double current = terminal.get(state, timepoint);
				
				production += current > 0. ? current : 0.;
			}
			
			return production;
		}
	};

	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double consumption = 0;
			
			for (Port<Double> terminal : balanceInputs)
			{
				double current = terminal.get(state, timepoint);
				
				consumption += current < 0. ? current : 0.;
			}
			
			return consumption;
		}
	};
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return productionOutput.get(state, timepoint) + consumptionOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return balanceOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> balanceWeightExpression = new Expression<Double>(balanceWeightOutput)
	{	
		@Override protected Double evaluate(State state, int timepoint)
		{
			return balanceWeight;	
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{	
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Math.abs(balanceOutput.get(state, timepoint))*balanceWeight;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return productionOutput.get(state, timepoint) >= -capacity && consumptionOutput.get(state, timepoint) <= capacity; 
		}
	};
	
	public Chart levelChart = new Timeline(balanceOutput);
	public Chart consumptionChart = new Timeline(consumptionOutput);
	public Chart productionChart = new Timeline(productionOutput);
	
}
