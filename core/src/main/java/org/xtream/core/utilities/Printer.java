package org.xtream.core.utilities;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public interface Printer<T extends Component>
{
	
	public void print(T component, State state, int timepoint);

}
