package org.xtream.core.optimizer.beam.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.optimizer.beam.Strategy;

public class KMeansStrategy implements Strategy
{
	
	private int rounds;
	
	public KMeansStrategy(int rounds)
	{
		this.rounds = rounds;
	}

	@Override
	public SortedMap<Key, List<State>> execute(List<State> currentStates, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint, Component root)
	{
		// Initialize state keys
		
		Map<State, Key> keys = new HashMap<>();
		
		for (State state : currentStates)
		{
			keys.put(state, new Key(root, state, timepoint));
		}
		
		// Initialize mittelwerte, i initial clusters
		
		SortedMap<Key, List<State>> clusters = new TreeMap<>(new Comparator<Key>()
			{
				@Override
				public int compare(Key o1, Key o2)
				{
					return o1.hashCode() - o2.hashCode();
				}
			}
		);
		
		for (int i = 0; i < classes; i++)
		{
			Key key = new Key(root, minEquivalences, maxEquivalences);
			
			clusters.put(key, new ArrayList<State>());
		}
		
		// Initialize cluster ids
		
		Map<Key, Integer> ids = new HashMap<>();
		
		for (Entry<Key, List<State>> id : clusters.entrySet())
		{
			ids.put(id.getKey(), id.getKey().hashCode());
		}
		
		// Rearrange states for defined number of rounds
		
		for (int round = 0; round < rounds; round++)
		{
			// Reset state to cluster mapping
			
			for (Entry<Key, List<State>> cluster : clusters.entrySet())
			{
				cluster.getValue().clear();
			}
			
			// Map states to clusters
			
			for (State state : currentStates)
			{
				// Initialize search for closest cluster
				
				Key key = keys.get(state);
				
				Entry<Key, List<State>> closest = clusters.entrySet().iterator().next();
				
				double distance = closest.getKey().calculateDistance(key);
				
				// Find closest cluster
				
				for (Entry<Key, List<State>> cluster : clusters.entrySet())
				{
					double temp = cluster.getKey().calculateDistance(key);
					
					if (temp < distance)
					{
						closest = cluster;
						
						distance = temp;
					}
				}
				
				// Register with closest cluster
				
				closest.getValue().add(state);
			}
			
			// Update cluster keys
	
			for (Entry<Key, List<State>> cluster : clusters.entrySet())
			{
				
				// Reset clusters to zero
				
				for (int i = 0; i < cluster.getKey().equivalences.length; i++)
				{
					cluster.getKey().equivalences[i] = 0;
				}
				
				// Check if states are connected to the cluster
				
				if (cluster.getValue().size() > 0)
				{
					// Accumulate the cluster dimensions
					
					for (State state : cluster.getValue())
					{
						Key key = keys.get(state);
						
						for (int i = 0; i < cluster.getKey().equivalences.length; i++)
						{
							cluster.getKey().equivalences[i] += key.equivalences[i];
						}
					}
					
					// Normalize the cluster dimensions
					
					for (int i = 0; i < cluster.getKey().equivalences.length; i++)
					{
						cluster.getKey().equivalences[i] /= cluster.getValue().size();
					}
				}
				else
				{
					// Initialize the cluster dimensions randomly
					
					for (int i = 0; i < cluster.getKey().equivalences.length; i++)
					{
						cluster.getKey().equivalences[i] = Math.random() * (maxEquivalences[i] - minEquivalences[i]) + minEquivalences[i];
					}
				}
			}
			
			// Check if anything changed

			boolean changed = false;
			
			for (Entry<Key, List<State>> cluster : clusters.entrySet())
			{
				int id = cluster.hashCode();
				
				for (State state : cluster.getValue())
				{
					id = id ^ state.hashCode();
				}
				
				if (id != ids.get(cluster.getKey()))
				{
					changed = true;
					
					ids.put(cluster.getKey(), id);
				}
			}
			
			// Stop iteration if nothing changed
			
			if (!changed)
			{
				break;
			}
		}
		
		// Remove empty clusters
		
		List<Key> emptyClusters = new ArrayList<>();
		
		for (Key cluster : clusters.keySet())
		{
			if (clusters.get(cluster).size() == 0)
			{
				emptyClusters.add(cluster);
			}
		}
		
		for (Key cluster : emptyClusters)
		{
			clusters.remove(cluster);
		}
		
		return clusters;
	}
}
