package org.xtream.core.workbench;

import org.xtream.core.model.containers.Component;

public class Event<T extends Component>
{
	
	public Part<T> source;
	
	public Event(Part<T> source)
	{
		this.source = source;
	}

}
