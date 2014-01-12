package org.xtream.core.model.expressions;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class ChannelExpression<T> extends Expression<T>
{
	
	@Constant
	public Port<T> source;

	public ChannelExpression(Port<T> port, Port<T> source)
	{
		super(port);
		
		this.source = source;
	}

	@Override
	public T evaluate(int timepoint)
	{
		return source.get(timepoint);
	}
		
}
