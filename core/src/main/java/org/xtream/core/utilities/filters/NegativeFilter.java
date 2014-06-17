package org.xtream.core.utilities.filters;

import org.xtream.core.model.Element;
import org.xtream.core.utilities.Filter;

public class NegativeFilter extends Filter
{
	
	private Filter filter;
	
	public NegativeFilter(Filter filter)
	{
		this.filter = filter;
	}

	@Override
	public boolean accept(Element element)
	{
		return !filter.accept(element);
	}

}
