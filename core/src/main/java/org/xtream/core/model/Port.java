package org.xtream.core.model;

import org.xtream.core.optimizer.State;

public class Port<T> extends Element
{

	public String name;
	public String qualifiedName;
	
	public Component parent;
	
	public Integer number;
	
	public Expression<T> expression;
	
	public State state;
	
	public final T get(int timepoint)
	{
		T value = state.get(this, timepoint);
		
		if (value == null && expression != null)
		{	
			value = expression.evaluate(timepoint);
			
			state.set(this, timepoint, value);
		}
		
		return value;
	}

}
