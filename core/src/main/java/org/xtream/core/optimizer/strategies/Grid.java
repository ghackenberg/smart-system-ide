package org.xtream.core.optimizer.strategies;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.containers.Component;
import org.xtream.core.model.State;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Strategy;

public class Grid implements Strategy
{
	private SortedMap<Key, List<State>> currentGroups;

	@Override
	public SortedMap<Key, List<State>> execute(List<State> currentStates, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint, Component root) {
		
		currentGroups = new TreeMap<>();
		
		for (State current : currentStates)
		{
			// Group Status
			
			Key currentKey = new Key(root, current, minEquivalences, maxEquivalences, classes, timepoint);
			
			List<State> currentGroup = currentGroups.get(currentKey);
			
			if (currentGroup == null)
			{
				currentGroup = new LinkedList<State>();
				
				currentGroups.put(currentKey, currentGroup);
			}
			
			// Check Status
			
			boolean dominant = true;
			
			for (int index = 0; index < currentGroup.size(); index++)
			{
				State alternative = currentGroup.get(index);
				
				Integer difference = current.comparePreferencesTo(alternative);
				
				if (difference != null)
				{
					if (difference < 0)
					{
						dominant = false;
						
						break; // do not keep
					}
					else if (difference == 0)
					{
						dominant = false;
						
						break; // do not keep
					}
					else if (difference > 0)
					{
						currentGroup.remove(index--);
						
						continue;
					}
					
					assert false;
				}
			}
			
			// Save Status
			
			if (dominant)
			{
				currentGroup.add(current);
			}
		}
		
		return currentGroups;
	}
	
}
