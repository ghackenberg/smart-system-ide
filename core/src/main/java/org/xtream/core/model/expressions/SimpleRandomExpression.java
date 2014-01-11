package org.xtream.core.model.expressions;

import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;

public abstract class SimpleRandomExpression<T> extends Expression<T>
{
	
	public SimpleRandomExpression(OutputPort<T> port)
	{
		super(port);
	}

	@Override
	public final T evaluate(int timepoint)
	{
		Set<T> set = evaluateSet(timepoint);
		
		int random = (int) Math.floor(Math.random() * set.size());
		
		for (T item : set)
		{
			if (--random < 0)
			{
				return item;
			}
		}
		
		throw new IllegalStateException();
	}
	
	protected abstract Set<T> evaluateSet(int timepoint);
	
}
