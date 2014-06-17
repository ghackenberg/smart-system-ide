package org.xtream.core.utilities;

import java.lang.reflect.Method;

import org.xtream.core.model.Element;
import org.xtream.core.utilities.filters.TrueFilter;

public abstract class Visitor
{
	
	private static final boolean REVERSE = false;
	private static final String METHOD = "handle";
	private static final Filter FILTER = new TrueFilter();
	
	public void traverse(Element element)
	{
		traverse(element, METHOD, REVERSE, FILTER);
	}
	
	public void traverse(Element element, Filter filter)
	{
		traverse(element, METHOD, REVERSE, filter);
	}
	
	public void traverse(Element element, boolean reverse)
	{
		traverse(element, METHOD, reverse, FILTER);
	}
	
	public void traverse(Element element, String method)
	{
		traverse(element, method, REVERSE, FILTER);
	}
	
	public void traverse(Element element, boolean reverse, Filter filter)
	{
		traverse(element, METHOD, reverse, filter);
	}
	
	public void traverse(Element element, String method, Filter filter)
	{
		traverse(element, method, REVERSE, filter);
	}
	
	public void traverse(Element element, String method, boolean reverse)
	{
		traverse(element, method, reverse, FILTER);
	}
	
	public void traverse(Element element, String method, boolean reverse, Filter filter)
	{
		for (int index = (reverse ? element.getChildren().size() - 1 : 0); (reverse ? index >= 0 : index < element.getChildren().size()); index = (reverse ? index - 1 : index + 1))
		{
			Element child = element.getChildren().get(index);
			
			if (filter.accept(child))
			{
				Class<?> typeIterator = child.getClass();
				
				while (Element.class.isAssignableFrom(typeIterator))
				{
					try
					{
						Method temp = getClass().getMethod(method, typeIterator);
						
						temp.invoke(this, child);
						
						break;
					}
					catch (Exception e)
					{
						typeIterator = typeIterator.getSuperclass();
					}
				}
			}
		}
	}

}
