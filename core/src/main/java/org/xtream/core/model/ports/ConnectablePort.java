package org.xtream.core.model.ports;

import org.xtream.core.model.Channel;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;


public class ConnectablePort<T> extends Port<T>
{

	@Constant
	public Channel<T> channel;

	@Override
	protected T evaluate(int timepoint)
	{
		return channel.source.get(timepoint);
	}

}
