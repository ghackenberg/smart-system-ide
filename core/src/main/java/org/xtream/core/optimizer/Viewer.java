package org.xtream.core.optimizer;

import org.xtream.core.model.Component;

public abstract class Viewer<T extends Component>
{
	
	public abstract void view(T component);

}
