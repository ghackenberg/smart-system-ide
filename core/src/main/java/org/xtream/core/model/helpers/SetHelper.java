package org.xtream.core.model.helpers;

import java.util.HashSet;
import java.util.Set;

public class SetHelper
{
	
	public static Set<Double> construct(double min, double max, int steps)
	{
		Set<Double> result = new HashSet<>();
		
		for (int i = 0; i <= steps; i++)
		{
			result.add(min + (max - min) * i / steps);
		}
		
		return result;
	}

}
