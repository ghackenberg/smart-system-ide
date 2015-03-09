package org.xtream.demo.infrastructure.datatypes.helpers;

import java.util.HashMap;

public class ColorManager
{
	public HashMap<String, Float> colorValues;
	
	public ColorManager() 
	{
		colorValues = new HashMap<String, Float>();

		// Fixed values for featured example		
		colorValues.put("OriginA", 0.33f);
		colorValues.put("OriginB", 0.66f);
		colorValues.put("OriginC", 0.99f);
		colorValues.put("DestinationA", 0.33f);
		colorValues.put("DestinationB", 0.66f);
		colorValues.put("DestinationC", 0.99f);
	}
	
	public float generateColor(String token, float threshold) 
	{
		if (!colorValues.containsKey(token)) 
		{
			float color = (float) Math.random();
		
			for (float item : colorValues.values())
			{
				if (Math.abs(color-item) <= threshold)
				{
					return generateColor(token, threshold);
				}
			}
			colorValues.put(token, color);
			return color;
		}
		else 
		{
			return colorValues.get(token);
		}
	}
}
