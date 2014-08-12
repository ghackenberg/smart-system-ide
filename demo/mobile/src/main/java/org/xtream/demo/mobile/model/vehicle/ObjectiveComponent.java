package org.xtream.demo.mobile.model.vehicle;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.mobile.datatypes.Graph;

public class ObjectiveComponent extends Component
{
	
	public ObjectiveComponent(Graph graph, Double timeWeight, Double powerWeight) 
	{
		this.graph = graph;
		this.timeWeight = timeWeight;
		this.powerWeight = powerWeight;
	}

	// Parameters
	public Graph graph;
	double timeWeight;
	double powerWeight;
	
	// Inputs

	public Port<Double> powerInput = new Port<>();
	public Port<Boolean> targetReachedInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Double> timeWeightOutput = new Port<>();
	public Port<Double> powerWeightOutput = new Port<>();
	public Port<Double> timeCostsOutput = new Port<>();
	public Port<Double> powerCostsOutput = new Port<>();

    public Port<Double> costsOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> timeCostsExpression = new Expression<Double>(timeCostsOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (targetReachedInput.get(state, timepoint) || !(drivingIndicatorInput.get(state, timepoint)))
			{
				return 0.;
			}
			else
			{
				return 1.;
			}
		}
	};
	
	public Expression<Double> powerCostsExpression = new Expression<Double>(powerCostsOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (targetReachedInput.get(state, timepoint) || !(drivingIndicatorInput.get(state, timepoint)))
			{
				return 0.;
			}
			else 
			{
				if (powerInput.get(state, timepoint) > 0)
				{
					return (powerInput.get(state, timepoint)/0.83);
				}
				else 
				{
					return 0.;
				}
			}
		}
	};
    
	public Expression<Double> costsExpression = new Expression<Double>(costsOutput)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{			
			return ((timeCostsOutput.get(state, timepoint)*timeWeight)+(powerCostsOutput.get(state, timepoint)*powerWeight));	
		}
	};
	
	public Expression<Double> timeWeightExpression = new Expression<Double>(timeWeightOutput)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{			
			return timeWeight;
		}
	};
	
	public Expression<Double> powerWeightExpression = new Expression<Double>(powerWeightOutput)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{			
			return powerWeight;
		}
	};
		
}
