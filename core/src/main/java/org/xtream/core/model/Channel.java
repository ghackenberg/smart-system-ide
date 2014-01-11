package org.xtream.core.model;

import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.ports.InputPort;
import org.xtream.core.model.ports.OutputPort;


public final class Channel<T>
{
	
	private class ChannelExpression extends Expression<T>
	{

		@Constant
		public Channel<T> channel;

		public ChannelExpression(Port<T> port, Channel<T> channel)
		{
			super(port);
			
			this.channel = channel;
		}

		@Override
		public T evaluate(int timepoint)
		{
			return channel.source.get(timepoint);
		}

	}
	
	public String name;
	
	public Port<T> source;
	public Port<T> target;
	
	public Expression<T> expression;
	
	public Channel(InputPort<T> source, InputPort<T> target)
	{
		this((Port<T>) source, (Port<T>) target);
	}
	
	public Channel(OutputPort<T> source, InputPort<T> target)
	{
		this((Port<T>) source, (Port<T>) target);
	}
	
	public Channel(OutputPort<T> source, OutputPort<T> target)
	{
		this((Port<T>) source, (Port<T>) target);
	}
	
	private Channel(Port<T> source, Port<T> target)
	{
		this.source = source;
		this.target = target;
		
		this.expression = new ChannelExpression(target, this);
	}

}
