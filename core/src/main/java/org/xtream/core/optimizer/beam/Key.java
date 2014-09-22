package org.xtream.core.optimizer.beam;

import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Equivalence;

public class Key implements Comparable<Key>
{
	
	public double[] equivalences;
	
	public Key()
	{
		equivalences = new double[0];
	}
	
	public Key(Component root)
	{
		equivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
		
		for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
		{
			equivalences[i] = Math.random();
		}
	}
	
	public Key(Component root, State state, double[] minEquivalences, double[] maxEquivalences, int timepoint)
	{
		equivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
		
		if (timepoint >= 0)
		{
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				equivalences[i] = (root.getDescendantsByClass(Equivalence.class).get(i).getPort().get(state, timepoint) - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]);
			}
		}
	}
	
	public Key(Component root, State state, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint)
	{
		equivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
		
		double scale = Math.pow(classes, 1. / root.getDescendantsByClass(Equivalence.class).size());
		
		if (timepoint >= 0)
		{
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				double originalValue = root.getDescendantsByClass(Equivalence.class).get(i).getPort().get(state, timepoint);
				double normalizedValue = (originalValue - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]) * scale;
				double discreteValue = Math.floor(normalizedValue);
				
				equivalences[i] = discreteValue;
			}
		}
	}

	@Override
	public int compareTo(Key other)
	{
		for (int index = 0; index < equivalences.length; index++)
		{
			double difference = equivalences[index] - other.equivalences[index];
			
			if (difference != 0)
			{
				// Difference found!
				return (int) Math.signum(difference);
			}
		}
		
		return 0;
	}
	
	public double calculateDistance(Key other)
	{
		double distance = 0;
		
		for (int i = 0; i < equivalences.length; i++)
		{
			double diff = equivalences[i] - other.equivalences[i];
			
			distance += diff * diff; 
		}
		
		return Math.sqrt(distance);
	}
	
	@Override
	public String toString()
	{
		String result = "key(";
		
		for (int i = 0; i < equivalences.length; i++)
		{
			result += (i > 0 ? "," : "") + equivalences[i];
		}
		
		result += ")";
		
		return result;
	}
	
}
