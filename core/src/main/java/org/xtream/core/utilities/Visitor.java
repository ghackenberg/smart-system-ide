package org.xtream.core.utilities;

import java.lang.reflect.Method;

import org.xtream.core.model.Element;

public abstract class Visitor
{
	
	public void visit(Element element)
	{
		Class<?> type;
		
		type = element.getClass();
		
		while (Element.class.isAssignableFrom(type))
		{
			try
			{
				Method method = getClass().getMethod("pre", type);
				
				method.invoke(this, element);
				
				break;
			}
			catch (Exception e)
			{
				type = type.getSuperclass();
			}
		}
		
		for (Element child : element.getChildren())
		{
			visit(child);
		}
		
		type = element.getClass();
		
		while (Element.class.isAssignableFrom(type))
		{
			try
			{
				Method method = getClass().getMethod("post", type);
				
				method.invoke(this, element);
				
				break;
			}
			catch (Exception e)
			{
				type = type.getSuperclass();
			}
		}
	}

}
