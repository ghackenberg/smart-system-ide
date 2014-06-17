package org.xtream.core.utilities.filters;

import org.xtream.core.model.Element;
import org.xtream.core.utilities.Filter;

public class TypeFilter extends Filter
{
	
	private Class<? extends Element> type;
	
	public TypeFilter(Class<? extends Element> type)
	{
		this.type = type;
	}

	@Override
	public boolean accept(Element element)
	{
		return type.isInstance(element);
	}

}
