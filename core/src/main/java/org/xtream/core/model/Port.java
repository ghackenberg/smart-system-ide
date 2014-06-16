package org.xtream.core.model;

import org.xtream.core.optimizer.State;

public class Port<T> extends Element
{
	
	private Integer number;
	
	@Reference
	private Expression<T> expression;
	
	private State state;
	
	public final T get(int timepoint)
	{
		T value = state.getValue(this, timepoint);
		
		if (value == null && expression != null)
		{	
			value = expression.evaluate(timepoint);
			
			state.setValue(this, timepoint, value);
		}
		
		return value;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	public Integer getNumber()
	{
		return number;
	}
	
	public void setExpression(Expression<T> expression)
	{
		this.expression = expression;
	}
	public Expression<T> getExpression()
	{
		return expression;
	}
	
	public void setState(State state)
	{
		this.state = state;
	}
	public State getState()
	{
		return state;
	}

}
