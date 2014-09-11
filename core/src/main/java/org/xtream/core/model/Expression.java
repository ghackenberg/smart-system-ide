package org.xtream.core.model;



public abstract class Expression<T> extends Element
{
	
	@Reference
	private Port<T> port;
	
	private boolean caching;
	
	private Integer number;

	public Expression(Port<T> port)
	{
		this(port, false);
	}
	
	public Expression(Port<T> port, boolean caching)
	{
		super(Expression.class.getClassLoader().getResource("elements/expression.png"));
		
		this.port = port;
		this.caching = caching;
		
		port.setExpression(this);
	}
	
	@Reference
	public Port<T> getPort()
	{
		return port;
	}
	
	public boolean getCaching()
	{
		return caching;
	}
	public void setCaching(boolean caching)
	{
		this.caching = caching;
	}
	
	public Integer getNumber()
	{
		return number;
	}
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	public final T get(State state, int timepoint)
	{
		if (timepoint >= 0)
		{
			if (caching)
			{
				T value = state.getValue(this, timepoint);
				
				if (value == null)
				{
					value = evaluate(state, timepoint);
					
					state.setValue(this, timepoint, value);
				}
				
				return value;
			}
			else
			{
				return evaluate(state, timepoint);
			}
		}
		else
		{
			throw new IllegalStateException(this.getQualifiedName() + ": Timepoint must not be negative (" + timepoint + ")!");
		}
	}
	
	protected abstract T evaluate(State state, int timepoint);
	
}
