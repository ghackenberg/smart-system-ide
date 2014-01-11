package org.xtream.core.optimizer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Worker implements Runnable
{
	public Component root;
	public int timepoint;
	public int groups;
	public int coverage;
	public double randomness;
	public Map<Key, List<State>> previousGroups;
	public Map<Key, List<State>> currentGroups;
	public Queue<Key> queue;
	
	public int generatedCount = 0;
	public int validCount = 0;
	
	public Worker(Component root, int timepoint, int groups, int coverage, double randomness, Map<Key, List<State>> previousGroups, Map<Key, List<State>> currentGroups, Queue<Key> queue)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.groups = groups;
		this.coverage = coverage;
		this.randomness = randomness;
		this.previousGroups = previousGroups;
		this.currentGroups = currentGroups;
		this.queue = queue;
	}
	
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Key previousKey = queue.remove();
						
				List<State> previousGroup = previousGroups.get(previousKey);
				
				for (int sample = 0; sample < Math.max(1, (double) coverage / groups); sample++)
				{
					generatedCount++;
					
					// Select Status
					
					State previous = previousGroup.get(0);
					
					if (sample > Math.max(1, (double) coverage / groups) * (1 - randomness))
					{
						int random = (int) Math.floor(Math.random() * previousGroup.size());
						
						previous = previousGroup.get(random);
					}
					
					previous.restore(root);
					
					// Create Status
					
					State current = new State(root.portsRecursive.size(), root.fieldsRecursive.size(), timepoint, previous);
					
					current.connect(root);
					
					for (Port<?> port : root.portsRecursive)
					{
						port.get(timepoint);
					}
					
					// Check Status
					
					boolean valid = true;
					
					for (Port<Boolean> constraint : root.constraintsRecursive)
					{
						valid = valid && constraint.get(timepoint);
					}
					
					if (valid)
					{
						validCount++;
						
						// Group Status
						
						Key currentKey = new Key(root, timepoint);
						
						List<State> currentGroup = currentGroups.get(currentKey);
						
						if (currentGroup == null)
						{
							currentGroup = Collections.synchronizedList(new LinkedList<State>());
							
							currentGroups.put(currentKey, currentGroup);
						}
						
						// Check Status
						
						boolean dominant = true;
						
						for (int index = 0; index < currentGroup.size(); index++)
						{
							State alternative = currentGroup.get(index);
							
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
									currentGroup.remove(index--);
									
									continue;
								}
								
								assert false;
							}
						}
						
						// Save Status
						
						if (dominant)
						{
							current.save();
							
							currentGroup.add(current);
						}
					}
				}
			}
		}
		catch (NoSuchElementException exception)
		{
			// Done!
		}
	}

}
