package org.xtream.core.model;

import org.xtream.core.model.ports.ConnectablePort;


public class Channel<T>
{
	
	public String name;
	
	public Port<T> source;
	public ConnectablePort<T> target;
	
	public Channel(Port<T> source, ConnectablePort<T> target)
	{
		this.source = source;
		this.target = target;
		
		target.channel = this;
	}

}
