package org.xtream.core.model.builders;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<T>
{
	
	public Map<T, Double> map = new HashMap<>();
	
	public MapBuilder<T> put(T key, Double value)
	{
		map.put(key, value);
		
		return this;
	}

}
