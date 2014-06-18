package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractQualitiesComponent;
import org.xtream.core.optimizer.State;

public class QualitiesComponent extends AbstractQualitiesComponent
{
	
	// Inputs
	
	public Port<Double> powerInput = new Port<>();
	
	public Port<Boolean> targetReachedInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Double> timeCostsOutput = new Port<>();
	public Port<Double> powerCostsOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> timeCostsExpression = new Expression<Double>(timeCostsOutput)	
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
	
	public Expression<Double> powerCostsExpression = new Expression<Double>(powerCostsOutput)	
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
	
}
