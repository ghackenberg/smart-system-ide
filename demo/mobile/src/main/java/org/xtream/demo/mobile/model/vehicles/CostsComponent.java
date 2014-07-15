package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.AbstractCostsComponent;

public class CostsComponent extends AbstractCostsComponent
{
	
	public CostsComponent(Double timeWeight, Double powerWeight) 
	{
		this.timeWeight = timeWeight;
		this.powerWeight = powerWeight;
	}

	// Parameters
	
	double timeWeight;
	double powerWeight;
	
	// Inputs
	
	public Port<Double> timeCostsInput = new Port<>();
	public Port<Double> powerCostsInput = new Port<>();

	// Outputs
	
	public Port<Double> timeWeightOutput = new Port<>();
	public Port<Double> powerWeightOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> costsExpression = new Expression<Double>(costsOutput)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{			
			return ((timeCostsInput.get(state, timepoint)*timeWeight)+(powerCostsInput.get(state, timepoint)*powerWeight));	
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
