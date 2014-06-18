package org.xtream.core.model;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xtream.core.utilities.Filter;

public abstract class Element
{
	
	private URL icon;
	
	private String name;
	private String qualifiedName;
	
	@Reference
	private Element parent;
	@Reference
	private List<Element> children = new ArrayList<>();
	@Reference
	private List<Element> descendants = new ArrayList<>();
	@Reference
	private Map<Class<?>, List<?>> childrenByClass = new HashMap<>();
	@Reference
	private Map<Class<?>, List<?>> descendantsByClass = new HashMap<>();
	@Reference
	private List<Expression<?>> cachingExpressions = new ArrayList<>(); 
	
	public Element()
	{
		this(Element.class.getClassLoader().getResource("element.png"));
	}
	public Element(URL icon)
	{
		this.icon = icon;
	}
	
	public URL getIcon()
	{
		return icon;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getQualifiedName()
	{
		return qualifiedName;
	}
	
	public Element getParent()
	{
		return parent;
	}
	
	public List<Element> getChildren()
	{
		return children;
	}
	
	public List<Element> getDescendants()
	{
		return descendants;
	}
	
	public List<Element> getChildren(Filter filter)
	{
		List<Element> result = new ArrayList<>();
		
		for (Element child : children)
		{
			if (filter.accept(child))
			{
				result.add(child);
			}
		}
		
		return result;
	}
	
	public List<Element> getDescendants(Filter filter)
	{
		List<Element> result = new ArrayList<>();
		
		for (Element child : descendants)
		{
			if (filter.accept(child))
			{
				result.add(child);
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getChildrenByClass(Class<T> type)
	{
		List<T> result = (List<T>) childrenByClass.get(type);
		
		if (result == null)
		{
			result = new ArrayList<>();
			
			childrenByClass.put(type, result);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getDescendantsByClass(Class<T> type)
	{
		List<T> result = (List<T>) descendantsByClass.get(type);
		
		if (result == null)
		{
			result = new ArrayList<>();
			
			descendantsByClass.put(type, result);
		}
		
		return result;
	}
	
	public List<Expression<?>> getCachingExpressions()
	{
		return cachingExpressions;
	}
	
	public void init(double caching)
	{
		init(null, "root", "root");
		
		int index = 0;
		
		for (Expression<?> expression : getDescendantsByClass(Expression.class))
		{
			expression.setCaching(expression.getCaching() || Math.random() < caching);
			
			if (expression.getCaching())
			{
				expression.setNumber(index++);
				
				cachingExpressions.add(expression);
			}
		}
	}
	
	private void init(Element parent, String name, String qualifiedName)
	{
		this.parent = parent;
		this.name = name;
		this.qualifiedName = qualifiedName;
		
		for (Field field : this.getClass().getFields())
		{
			try
			{
				if (field.getAnnotation(Reference.class) == null)
				{
					Object object = field.get(this);
					
					if (object != null)
					{
						if (field.getType().isArray())
						{
							Object[] objects = (Object[]) object;
							
							for (int i = 0; i < objects.length; i++)
							{
								if (objects[i] != null)
								{
									load(field, objects[i], field.getName() + "[" + i + "]", qualifiedName + "." + field.getName() + "[" + i + "]");
								}
							}
						}
						else
						{
							load(field, object, field.getName(), qualifiedName + "." + field.getName());
						}
					}
				}
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void load(Field field, Object object, String name, String qualifiedName)
	{
		if (object instanceof Element)
		{
			// Init
			
			Element element = (Element) object;
			
			element.init(this, name, qualifiedName);
			
			// Children / descendants
			
			children.add(element);
			
			descendants.add(element);
			descendants.addAll(element.descendants);
			
			// Descendants by class
			
			for (Entry<Class<?>, List<?>> entry : element.descendantsByClass.entrySet())
			{
				List descendants = descendantsByClass.get(entry.getKey());
				
				if (descendants == null)
				{
					descendants = new ArrayList<>();
					
					descendantsByClass.put(entry.getKey(), descendants);
				}
				
				descendants.addAll(entry.getValue());
			}
			
			// Children / descendants by class
			
			Class<?> type = element.getClass();
			
			while (Element.class.isAssignableFrom(type))
			{
				List children = childrenByClass.get(type);
				List descendants = descendantsByClass.get(type);
				
				if (children == null)
				{
					children = new ArrayList<>();
					
					childrenByClass.put(type, children);
				}
				
				if (descendants == null)
				{
					descendants = new ArrayList<>();
					
					descendantsByClass.put(type, descendants);
				}
				
				children.add(element);
				descendants.add(element);
				
				type = type.getSuperclass();
			}
		}
	}
	
	public void dump(PrintStream out)
	{
		dump(out, 0);
	}
	
	private void dump(PrintStream out, int indent)
	{
		tabs(out, indent);
		
		out.println(qualifiedName);
		
		for (Element element : children)
		{
			tabs(out, indent + 1);
			
			out.println(element.qualifiedName);
			
			element.dump(out, indent + 1);
		}
	}
	
	private void tabs(PrintStream out, int indent)
	{
		for (int i = 0; i < indent; i++)
		{
			out.print("\t");
		}
	}

}
