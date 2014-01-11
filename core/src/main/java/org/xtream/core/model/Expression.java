package org.xtream.core.model;

import org.xtream.core.model.annotations.Constant;

public abstract class Expression<T>
{
	
	@Constant
	public String name;
	@Constant
	public Port<T> port;

	public Expression(Port<T> port)
	{
		this.port = port;
		
		port.expression = this;
	}
	
	public abstract T evaluate(int timepoint);
	
}
