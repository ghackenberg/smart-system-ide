package org.xtream.core.utilities.viewers;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.utilities.Viewer;

public class CompositeViewer<T extends Component> implements Viewer<T>
{
	
	private List<Viewer<T>> viewers = new ArrayList<>();
	
	@SafeVarargs
	public CompositeViewer(Viewer<T>... viewers)
	{
		for (Viewer<T> viewer : viewers)
		{
			this.viewers.add(viewer);
		}
	}
	
	public void add(Viewer<T> viewer)
	{
		viewers.add(viewer);
	}
	
	public void remove(Viewer<T> viewer)
	{
		viewers.remove(viewer);
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
