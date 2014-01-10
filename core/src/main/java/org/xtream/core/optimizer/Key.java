package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Key implements Comparable<Key>
{
	
	public List<Double> id = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public Key(Component root, int timepoint)
	{
		for (Port<?> port : root.equivalencesRecursive)
		{
			if ((Port<Double>) port != null)
			{
				id.add(((Port<Double>) port).get(timepoint));
			}
		}
	}

	@Override
	public int compareTo(Key other)
	{
		for (int index = 0; index < id.size(); index++)
		{
			double difference = id.get(index) - other.id.get(index);
			
			if (difference != 0)
			{
				return (int) Math.signum(difference);
			}
		}
		
		return 0;
	}
	
}
