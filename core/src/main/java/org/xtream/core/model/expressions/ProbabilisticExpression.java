package org.xtream.core.model.expressions;

import java.util.Map;
import java.util.Map.Entry;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public abstract class ProbabilisticExpression<T> extends Expression<T>
{
	
	public ProbabilisticExpression(Port<T> port)
	{
		super(port);
	}

	@Override
	public final T evaluate(int timepoint)
	{
		Map<T, Double> distribution = evaluateDistribution(timepoint);
		
		double sum = 0;
		
		for (Entry<T, Double> entry : distribution.entrySet())
		{
			sum += entry.getValue();
		}
		
		double random = Math.random() * sum;
		
		for (Entry<T, Double> entry : distribution.entrySet())
		{
			if ((random -= entry.getValue()) < 0)
			{
				return entry.getKey();
			}
		}
		
		throw new IllegalStateException();
	}
	
	protected abstract Map<T, Double> evaluateDistribution(int timepoint);
	
}
