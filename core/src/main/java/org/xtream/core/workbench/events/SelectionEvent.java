package org.xtream.core.workbench.events;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.model.Element;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;

public class SelectionEvent<T extends Component> extends Event<T>
{
	
	private List<Element> elements;
	
	public SelectionEvent(Part<T> source, Element element)
	{
		super(source);
		
		elements = new ArrayList<>();
		
		elements.add(element);
	}
	
	@SafeVarargs
	public SelectionEvent(Part<T> source, Element... elements)
	{
		super(source);
		
		this.elements = new ArrayList<>();
		
		for (Element object : elements)
		{
			this.elements.add(object);
		}
	}
	
	public SelectionEvent(Part<T> source, List<Element> elements)
	{
		super(source);
		
		this.elements = elements;
	}
	
	public List<Element> getElements()
	{
		return elements;
	}
	
	public Element getElement()
	{
		if (elements.size() == 1)
		{
			return elements.get(0);
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <S extends Element> List<S> getElementsByClass(Class<S> type)
	{
		List<S> result = new ArrayList<>();
		
		for (Element element : elements)
		{
			if (type.isInstance(element))
			{
				result.add((S) element);
			}
		}
		
		return result;
	}
	
	public <S extends Element> S getElementByClass(Class<S> type)
	{
		List<S> elements = getElementsByClass(type);
		
		if (elements.size() == 1)
		{
			return elements.get(0);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

}
