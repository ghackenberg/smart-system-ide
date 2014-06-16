package org.xtream.core.utilities;

import org.xtream.core.model.Component;

public interface Viewer<T extends Component>
{
	
	public void view(T component);

}
