package org.xtream.demo.mobile.datatypes.helpers;

import java.util.HashMap;

public class ColorManager
{
	public HashMap<String, Float> colorValues;
	
	public ColorManager() 
	{
		colorValues = new HashMap<String, Float>();
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
