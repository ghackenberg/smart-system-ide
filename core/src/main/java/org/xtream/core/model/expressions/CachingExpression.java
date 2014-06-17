package org.xtream.core.model.expressions;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public abstract class CachingExpression<T> extends Expression<T>
{
	
	private Integer number;

	public CachingExpression(Port<T> port)
	{
		super(port);
	}
	
	public Integer getNumber()
	{
		return number;
	}
	public void setNumber(Integer number)
	{
		this.number = number;
	}

	@Override
	public final T evaluate(State state, int timepoint)
	{
		T value = state.getValue(this, timepoint);
		
		if (value == null)
		{
			value = evaluateInternal(state, timepoint);
			
			state.setValue(this, timepoint, value);
		}
		
		return value;
	}
	
	protected abstract T evaluateInternal(State state, int timepoint);

}
