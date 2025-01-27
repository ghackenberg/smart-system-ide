package org.xtream.core.optimizer;

import org.xtream.core.model.Component;
import org.xtream.core.model.State;

public abstract class Engine<T extends Component>
{
	
	protected T root;
	
	public Engine(T root)
	{
		this.root = root;
	}
	
	public T getRoot()
	{
		return root;
	}
	
	public abstract State run(int duration, Monitor<T> monitor);

}
