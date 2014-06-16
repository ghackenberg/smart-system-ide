package org.xtream.core.utilities;

import org.xtream.core.model.Component;

public interface Printer<T extends Component>
{
	
	public void print(T component, int timepoint);

}
