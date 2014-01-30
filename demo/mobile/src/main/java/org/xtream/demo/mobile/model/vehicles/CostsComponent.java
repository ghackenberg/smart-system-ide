package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractCostsComponent;

public class CostsComponent extends AbstractCostsComponent
{
	
	public CostsComponent(Double positionWeight, Double timeWeight, Double powerWeight, Double chargeStateWeight) 
	{
		this.positionWeight = positionWeight;
		this.timeWeight = timeWeight;
		this.powerWeight = powerWeight;
		this.chargeStateWeight = chargeStateWeight;
	}

	// Parameters
	
	double positionWeight;
	double timeWeight;
	double powerWeight;
	double chargeStateWeight;
	
	// Inputs
	
	public Port<Double> positionCostsInput = new Port<>();
	public Port<Double> timeCostsInput = new Port<>();
	public Port<Double> powerCostsInput = new Port<>();
	public Port<Double> chargeStateCostsInput = new Port<>();

	// Outputs
	
	public Port<Double> positionWeightOutput = new Port<>();
	public Port<Double> timeWeightOutput = new Port<>();
	public Port<Double> powerWeightOutput = new Port<>();
	public Port<Double> chargeStateWeightOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> costsExpression = new Expression<Double>(costsOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{			
			return ((positionCostsInput.get(timepoint)*positionWeight)+(timeCostsInput.get(timepoint)*timeWeight)+(powerCostsInput.get(timepoint)*powerWeight)+(chargeStateCostsInput.get(timepoint)*chargeStateWeight));	
		}
	};
	
	public Expression<Double> positionWeightExpression = new Expression<Double>(positionWeightOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{			
			return positionWeight;
		}
	};
	
	public Expression<Double> timeWeightExpression = new Expression<Double>(timeWeightOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{			
			return timeWeight;
		}
	};
	
	public Expression<Double> powerWeightExpression = new Expression<Double>(powerWeightOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{			
			return powerWeight;
		}
	};
	
	public Expression<Double> chargeStateWeightExpression = new Expression<Double>(chargeStateWeightOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{			
			return chargeStateWeight;
		}
	};
		
}
