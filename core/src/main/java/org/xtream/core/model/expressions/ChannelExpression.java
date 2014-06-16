package org.xtream.core.model.expressions;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class ChannelExpression<T> extends Expression<T>
{

	@Reference
	private Port<T> source;

	public ChannelExpression(Port<T> port, Port<T> source)
	{
		super(port);
		
		this.source = source;
	}
	
	public Port<T> getSource()
	{
		return source;
	}

	@Override
	public T evaluate(int timepoint)
	{
		return source.get(timepoint);
	}
		
}
