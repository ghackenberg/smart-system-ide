package org.xtream.core.optimizer.beam.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.optimizer.beam.Strategy;

public class RandomStrategy implements Strategy
{
	
	@Override
	public SortedMap<Key, List<State>> execute(List<State> currentStates, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint, Component root)
	{
		// Initialize cluster data structure
		
		SortedMap<Key, List<State>> currentGroups = new TreeMap<>(new Comparator<Key>()
			{
				@Override
				public int compare(Key o1, Key o2)
				{
					return o1.hashCode() - o2.hashCode();
				}
			}
		);
		
		// Initialize key store
		
		List<Key> keys = new ArrayList<Key>();
		
		// Create keys and cluster lists
		
		for (int i = 0; i < classes; i++)
		{
			Key key = new Key();
			
			currentGroups.put(key, new ArrayList<State>());
			
			keys.add(key);
		}
		
		// Assign states to clusters randomly
		
		for (State current : currentStates)
		{
			int i = (int) Math.floor(Math.random() * classes);
			
			currentGroups.get(keys.get(i)).add(current);
		}
		
		// Remove empty clusters
		
		List<Key> emptyClusters = new ArrayList<>();
		
		for (Key cluster : currentGroups.keySet())
		{
			if (currentGroups.get(cluster).size() == 0)
			{
				emptyClusters.add(cluster);
			}
		}
		
		for (Key cluster : emptyClusters)
		{
			currentGroups.remove(cluster);
		}
		
		// Return clusters
		
		return currentGroups;
	}
	
}
