package org.xtream.core.model;

import org.xtream.core.model.annotations.Constant;
import org.xtream.core.optimizer.State;

public abstract class Port<T>
{
	
	@Constant
	public String name;
	
	public State state = null;
	
	public final T get(int timepoint)
	{
		T value = state.get(this, timepoint);
		
		if (value == null)
		{
			value = evaluate(timepoint);
			
			state.set(this, timepoint, value);
		}
		
		return value;
	}
	
	protected abstract T evaluate(int timepoint);

}
