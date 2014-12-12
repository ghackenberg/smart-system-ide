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
			Equivalence equivalence = root.getDescendantsByClass(Equivalence.class).get(i);
			
			equivalences[i] = Math.random() * equivalence.getWeight();
		}
	}
	
	public Key(Component root, State state, double[] minEquivalences, double[] maxEquivalences, int timepoint)
	{
		equivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
		
		if (timepoint >= 0)
		{
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				if (minEquivalences[i] != maxEquivalences[i])
				{
					Equivalence equivalence = root.getDescendantsByClass(Equivalence.class).get(i);
					
					equivalences[i] = (equivalence.getPort().get(state, timepoint) - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]) * equivalence.getWeight();
				}
				else
				{
					equivalences[i] = 0;
				}
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
				Equivalence equivalence = root.getDescendantsByClass(Equivalence.class).get(i);
				
				if (minEquivalences[i] != maxEquivalences[i])
				{
					double originalValue = equivalence.getPort().get(state, timepoint);
					double normalizedValue = (originalValue - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]) * scale;
					double discreteValue = Math.floor(normalizedValue);
					
					equivalences[i] = discreteValue;
				}
				else
				{
					equivalences[i] = 0;
				}
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
