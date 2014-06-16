package org.xtream.core.utilities.helpers;

import java.util.HashSet;
import java.util.Set;

public class SetHelper
{
	
	public static Set<Double> construct(double min, double max, int steps)
	{
		Set<Double> set = new HashSet<>();
		
		for (int step = 0; step <= steps; step++)
		{
			set.add(min + step / steps * (max - min));
		}
		
		return set;
	}

}
