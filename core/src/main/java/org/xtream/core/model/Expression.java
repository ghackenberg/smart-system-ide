package org.xtream.core.model;

import org.xtream.core.optimizer.State;


public abstract class Expression<T> extends Element
{
	
	@Reference
	private Port<T> port;

	public Expression(Port<T> port)
	{
		this.port = port;
		
		port.setExpression(this);
	}
	
	public Port<T> getPort()
	{
		return port;
	}
	
	public abstract T evaluate(State state, int timepoint);
	
}
