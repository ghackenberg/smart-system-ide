package org.xtream.core.model.builders;

import java.util.HashSet;
import java.util.Set;


public class SetBuilder<T>
{
	
	public Set<T> set = new HashSet<>();
	
	public SetBuilder<T> add(T value)
	{
		set.add(value);
		
		return this;
	}

}
