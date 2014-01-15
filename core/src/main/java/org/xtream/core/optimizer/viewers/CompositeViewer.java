package org.xtream.core.optimizer.viewers;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Viewer;

public class CompositeViewer<T extends Component> extends Viewer<T>
{
	
	private Viewer<T>[] viewers;
	
	@SafeVarargs
	public CompositeViewer(Viewer<T>... viewers)
	{
		this.viewers = viewers;
	}

	@Override
	public void view(T component)
	{
		for (Viewer<T> viewer : viewers)
		{
			viewer.view(component);
		}
	}

}
