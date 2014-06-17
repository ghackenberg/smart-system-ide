package org.xtream.core.utilities.filters;

import org.xtream.core.model.Element;
import org.xtream.core.utilities.Filter;

public class TrueFilter extends Filter
{
	
	@Override
	public boolean accept(Element element)
	{
		return true;
	}

}
