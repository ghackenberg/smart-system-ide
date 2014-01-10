package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Engine
{
	
	public Class<? extends Component> type;
	public Component root;
	
	public Engine(Class<? extends Component> type)
	{
		this.type = type;
		
		try
		{
			root = type.newInstance();
			root.init();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run(int duration, int coverage, double randomness)
	{
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<>();
		
		State start = new State(root);
		
		start.save();
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(start);
		
		previousGroups.put(new Key(root, -1), initialGroup);
		
		// Run optimization
		
		int timepoint;
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			SortedMap<Key, List<State>> currentGroups = new TreeMap<>();
			
			// Prepare statistics
			
			int validCount = 0;
			int invalidCount = 0;
			int dominatedCount = 0;
			int uncomparableCount = 0;
			
			// Interate through all equivalence classes
			
			for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
			{
				// Generate the requested amount of samples for the equivalence class
				
				for (int sample = 0; sample < Math.max(1, (double) coverage / previousGroups.size()); sample++)
				{
					// Select state
					
					State previous = previousGroup.getValue().get(0);
					
					if (sample > Math.max(1, (double) coverage / previousGroups.size()) * (1 - randomness))
					{
						int random = (int) Math.floor(Math.random() * previousGroup.getValue().size());
						
						previous = previousGroup.getValue().get(random);
					}
					
					previous.load();
					
					// Create state
					
					State current = new State(root, timepoint, previous);
					
					for (Port<?> port : root.portsRecursive)
					{
						port.get(timepoint);
					}
					
					// Check state
					
					boolean valid = true;
					
					for (Port<Boolean> constraint : root.constraintsRecursive)
					{
						valid = valid && constraint.get(timepoint);
					}
					
					if (valid)
					{
						// Group state
						
						Key key = new Key(root, timepoint);
						
						List<State> group = currentGroups.get(key);
						
						if (group == null)
						{
							group = new ArrayList<>();
							
							currentGroups.put(key, group);
						}
						
						// Check state
						
						boolean dominant = true;
						
						for (int index = 0; index < group.size(); index++)
						{
							State alternative = group.get(index);
							
							Integer difference = current.compareDominanceTo(alternative);
							
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
									group.remove(index--);
									
									dominatedCount++;
									
									continue;
								}
								
								throw new IllegalStateException();
							}
							else
							{
								uncomparableCount++;
							}
						}
						
						// Save state
						
						if (dominant)
						{
							current.save();
							
							group.add(current);
						}
						else
						{
							dominatedCount++;
						}
					}
					else
					{
						invalidCount++;
					}
				}
			}
			
			// Prepare next iteration
			
			if (currentGroups.size() > 0)
			{
				for (Entry<Key, List<State>> currentGroup : currentGroups.entrySet())
				{
					Collections.sort(currentGroup.getValue());
					
					validCount += currentGroup.getValue().size();
				}
					
				previousGroups = currentGroups;
				
				// Print result
				
				System.out.println("Timepoint " + timepoint + " = " + validCount + " / " + invalidCount + " / " + dominatedCount + " / " + uncomparableCount + " / " + previousGroups.size());
			}
			else
			{
				break; // Stop optimization
			}
		}
		
		// Select best
		
		State best = previousGroups.get(previousGroups.firstKey()).get(0);
		
		for (Entry<Key, List<State>> entry : previousGroups.entrySet())
		{
			for (Port<Double> port : root.minObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) > entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
			for (Port<Double> port : root.maxObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) < entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
		}
		
		best.load();
		
		// Print best
		
		for (int i = 0; i < timepoint; i++)
		{
			System.out.println();
			System.out.println("Timepoint " + i);
			System.out.println();
			
			for (Port<?> port : root.portsRecursive)
			{
				System.out.println(port.name + " = " + port.get(i));
			}
		}
	}

}
