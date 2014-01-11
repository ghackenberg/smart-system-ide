package org.xtream.core.model;

import org.xtream.core.optimizer.State;

public abstract class Port<T>
{
	
	public String name;
	public Integer number;
	public Expression<T> expression;
	public State state;
	
	public final T get(int timepoint)
	{
		T value = state.get(this, timepoint);
		
		if (value == null)
		{
			value = expression.evaluate(timepoint);
			
			state.set(this, timepoint, value);
		}
		
		return value;
	}

}
