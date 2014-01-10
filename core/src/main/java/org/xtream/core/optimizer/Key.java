package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Key implements Comparable<Key>
{
	
	public Double minObjective;
	public Double maxObjective;
	
	public List<Double> equivalences = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public Key(Component root, int timepoint)
	{
		for (Port<Double> port : root.minObjectivesRecursive)
		{
			minObjective = port.get(timepoint);
		}
		for (Port<Double> port : root.maxObjectivesRecursive)
		{
			maxObjective = port.get(timepoint);
		}
		
		for (Port<?> port : root.equivalencesRecursive)
		{
			if ((Port<Double>) port != null)
			{
				equivalences.add(((Port<Double>) port).get(timepoint));
			}
		}
	}

	@Override
	public int compareTo(Key other)
	{
		for (int index = 0; index < equivalences.size(); index++)
		{
			double difference = equivalences.get(index) - other.equivalences.get(index);
			
			if (difference != 0)
			{
				// Difference found!
				return (int) Math.signum(difference);
			}
		}
		
		return 0;
	}
	
}
