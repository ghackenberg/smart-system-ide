package org.xtream.core.optimizer;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public abstract class Engine<T extends Component>
{
	
	protected T root;
	
	public Engine(T root)
	{
		this.root = root;
	}
	
	public abstract State run(int duration, Monitor<T> monitor);

}
