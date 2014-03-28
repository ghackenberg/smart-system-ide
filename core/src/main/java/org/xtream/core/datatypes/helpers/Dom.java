package org.xtream.core.datatypes.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Dom
{
	
	public static List<Element> getChildElements(Element parent)
	{
		List<Element> result = new ArrayList<Element>();
		
		for (int i = 0; i < parent.getChildNodes().getLength(); i++)
		{
			Node child = parent.getChildNodes().item(i);
			
			if (child.getNodeType() == Node.ELEMENT_NODE)
			{
				result.add((Element) child);
			}
		}
		
		return result;
	}
	
	public static Element getChildElement(Element parent)
	{
		Collection<Element> result = getChildElements(parent);
		
		if (result.size() == 0)
		{
			return null;
		}
		else if (result.size() == 1)
		{
			return result.iterator().next();
		}
		else
		{
			throw new IllegalArgumentException("Exactly one child element required.");	
		}
	}
	
	public static List<Element> getChildElementsByTagname(Element parent, String tagname)
	{
		List<Element> result = new ArrayList<Element>();
		
		for (Element element : getChildElements(parent))
		{
			if (element.getTagName().equals(tagname))
			{
				result.add(element);
			}
		}
		
		return result;
	}
	
	public static Element getChildElementByTagname(Element parent, String tagname)
	{
		Collection<Element> result = getChildElementsByTagname(parent, tagname);
		
		if (result.size() == 0)
		{
			return null;
		}
		else if (result.size() == 1)
		{
			return result.iterator().next();
		}
		else
		{	
			throw new IllegalArgumentException("Exactly one child element required.");
		}
		
	}

}
