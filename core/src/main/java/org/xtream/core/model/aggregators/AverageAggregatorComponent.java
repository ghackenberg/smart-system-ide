package org.xtream.core.model.aggregators;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AggregatorComponent;

public class AverageAggregatorComponent extends AggregatorComponent
{
	
	public AverageAggregatorComponent(int size)
	{
		super(size);
	}
	
	// Expressions
	
	public Expression<Double> valueExpression = new Expression<Double>(valueOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double sum = 0.;
			
			for (Port<Double> item : itemInputs)
			{
				sum += item.get(timepoint) / itemInputs.length;
			}
			
			return sum;
		}
	};

}
