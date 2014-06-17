package org.xtream.core.utilities;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.State;

public interface Printer<T extends Component>
{
	
	public void print(T component, State state, int timepoint);

}
