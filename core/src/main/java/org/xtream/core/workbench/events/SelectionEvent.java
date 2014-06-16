package org.xtream.core.workbench.events;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;

public class SelectionEvent<T extends Component> extends Event<T>
{
	
	public List<Object> objects;
	
	public SelectionEvent(Part<T> source, Object object)
	{
		super(source);
		
		objects = new ArrayList<>();
		
		objects.add(object);
	}
	
	@SafeVarargs
	public SelectionEvent(Part<T> source, Object... objects)
	{
		super(source);
		
		this.objects = new ArrayList<>();
		
		for (Object object : objects)
		{
			this.objects.add(object);
		}
	}
	
	public SelectionEvent(Part<T> source, List<Object> objects)
	{
		super(source);
		
		this.objects = objects;
	}

}
