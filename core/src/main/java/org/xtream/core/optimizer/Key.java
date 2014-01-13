package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.Component;

public class Key implements Comparable<Key>
{
	
	public List<Double> equivalences = new ArrayList<>();
	
	public Key(Component root, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint)
	{
		double scale = Math.pow(classes, 1. / root.equivalencesRecursive.size());
		
		if (timepoint >= 0)
		{
			for (int i = 0; i < root.equivalencesRecursive.size(); i++)
			{
				double discreteValue = root.equivalencesRecursive.get(0).port.get(timepoint);
				double normalizedValue = (discreteValue - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]) * scale;
				
				equivalences.add(Math.floor(normalizedValue));
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
