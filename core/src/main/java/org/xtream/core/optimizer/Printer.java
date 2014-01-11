package org.xtream.core.optimizer;

import org.xtream.core.model.Component;

public abstract class Printer<T extends Component>
{
	
	public abstract void print(T component, int timepoint);

}
