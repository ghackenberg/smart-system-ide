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
		if (timepoint >= 0)
		{
			if (expression != null) 
			{
				return expression.get(state, timepoint);
			}
			else 
			{
				throw new IllegalStateException(this.getQualifiedName() + ": Expression required!");
			}
		}
		else
		{
			throw new IllegalStateException(this.getQualifiedName() + ": Timepoint must be positive (" + timepoint + ")!");
		}
	}
	
	public void setExpression(Expression<T> expression)
	{
		this.expression = expression;
	}
	@Reference
	public Expression<T> getExpression()
	{
		return expression;
	}

}
