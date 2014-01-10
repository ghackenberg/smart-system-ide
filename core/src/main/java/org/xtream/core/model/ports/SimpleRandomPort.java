package org.xtream.core.model.ports;

import java.util.Set;

import org.xtream.core.model.Port;

public abstract class SimpleRandomPort<T> extends Port<T>
{
	
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
