package org.xtream.core.model;

import org.xtream.core.optimizer.State;


public class Port<T> extends Element
{
	
	@Reference
	private Expression<T> expression;
	
	public T get(State state, int timepoint)
	{
		return expression.evaluate(state, timepoint);
	}
	
	public void setExpression(Expression<T> expression)
	{
		this.expression = expression;
	}
	public Expression<T> getExpression()
	{
		return expression;
	}

}
