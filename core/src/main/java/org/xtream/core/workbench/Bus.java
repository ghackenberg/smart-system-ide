package org.xtream.core.workbench;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.containers.Component;

public class Bus<T extends Component>
{
	
	public List<Part<T>> components = new ArrayList<>();
	
	public void broadcast(Event<T> event)
	{
		for (Part<T> part : components)
		{
			part.trigger(event);
		}
	}

}
