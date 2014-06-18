package org.xtream.core.utilities.filters;

import org.xtream.core.model.Element;
import org.xtream.core.model.Expression;
import org.xtream.core.utilities.Filter;

public class CachingFilter extends Filter
{

	@Override
	public boolean accept(Element element)
	{
		if (element instanceof Expression)
		{
			Expression<?> expression = (Expression<?>) element;
			
			return expression.getCaching();
		}
		
		return false;
	}

}
