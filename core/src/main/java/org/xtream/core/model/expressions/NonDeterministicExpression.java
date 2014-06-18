package org.xtream.core.model.expressions;

import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public abstract class NonDeterministicExpression<T> extends Expression<T>
{
	
	public NonDeterministicExpression(Port<T> port)
	{
		super(port, true);
	}

	@Override
	protected final T evaluate(State state, int timepoint)
	{
		Set<T> set = evaluateSet(state, timepoint);
		
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
	
	protected abstract Set<T> evaluateSet(State state, int timepoint);
	
}
