package org.xtream.core.optimizer.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Strategy;

public class KMeans implements Strategy
{

	private SortedMap<Key, List<State>> currentGroups;
	private HashMap<Integer, Key> keyMap; 

	@Override
	public SortedMap<Key, List<State>> execute(List<State> currentStates, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint, Component root) {
		
		currentGroups = new TreeMap<>();
		
		// Initialize mittelwerte, i initial clusters
		
		for (int i = 0; i < classes; i++)
		{
			currentGroups.put(new Key(root), new ArrayList<State>());
		}
		
		// Find distances
		
		for (State current : currentStates)
		{
			// Cached key calculation
			
			int hashValue = Objects.hash(root, current, minEquivalences, maxEquivalences, classes, timepoint);
			Key stateKey = null;
			
			if (keyMap.containsKey(hashValue))
			{
				stateKey = keyMap.get(hashValue);
			}
			else {
				stateKey = new Key(root, current, minEquivalences, maxEquivalences, classes, timepoint);
			}
			
			// Find closest cluster
			
			double bestDistance = Double.MAX_VALUE;
			Entry<Key, List<State>> bestCluster = null;
			
			for (Entry<Key, List<State>> cluster : currentGroups.entrySet())
			{
				double distance = stateKey.calculateDistance(cluster.getKey());
				
				if (distance < bestDistance)
				{
					bestDistance = distance;
					bestCluster = cluster;
				}
			}
			
			// Add state to cluster
			
			bestCluster.getValue().add(current);

			// Add to currentGroups
			currentGroups.entrySet().add(bestCluster);
		}
		
		// TODO Move cluster mittelwerte
		
		int iteration = 0;
		int maxIterations = 100;
		boolean valueChanged = true;
		
		while (valueChanged && (iteration < maxIterations))
		{
			valueChanged = false;
			
			for (Entry<Key, List<State>> cluster : currentGroups.entrySet())
			{
				// Update Clusters
				
				@SuppressWarnings("unused")
				Entry<Key, List<State>> previousCluster = cluster;
				
				double centroid = cluster.getKey().getCentroid();
				
				@SuppressWarnings("unused")
				double distance = cluster.getKey().calculateDistanceDouble(centroid);
				
			}
			
			// Calculate distance and update 
			for (@SuppressWarnings("unused") State current : currentStates) {
				
				// bestDistance of entities and clusters
				double bestDistance = Double.MAX_VALUE;
				double distance = 0.0;
				
				
				if (distance < bestDistance) {
					distance = bestDistance;
					valueChanged = true;
				}
				
			}
				
			iteration++;
		}
		
		return currentGroups;
		
	}
}
