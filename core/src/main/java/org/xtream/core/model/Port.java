package org.xtream.core.model;



public class Port<T> extends Element
{
	
	@Reference
	private Expression<T> expression;
	
	public Port()
	{
		super(Port.class.getClassLoader().getResource("elements/port.png"));
	}
	
	public T get(State state, int timepoint)
	{
		if (expression != null) 
		{
			return expression.get(state, timepoint);
		}
		else 
		{
			throw new IllegalStateException(this.getQualifiedName());
		}
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
