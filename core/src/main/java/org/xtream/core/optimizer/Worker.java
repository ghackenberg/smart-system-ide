package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;

public class Worker implements Runnable
{
	public Component root;
	public int timepoint;
	public int coverage;
	public double randomness;
	public Map<Key, List<State>> previousGroups;
	public Queue<Key> queue;
	
	public int generatedCount = 0;
	public int validCount = 0;
	public List<State> currentStates = new ArrayList<>();
	
	public Worker(Component root, int timepoint, int coverage, double randomness, Map<Key, List<State>> previousGroups, Queue<Key> queue)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.coverage = coverage;
		this.randomness = randomness;
		this.previousGroups = previousGroups;
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
				
				double count = Math.max(1, (double) coverage / previousGroups.size());
				
				for (int sample = 0; sample < count; sample++)
				{
					generatedCount++;
					
					// Select Status
					
					State previous = previousGroup.get(0);
					
					if (sample > count * (1 - randomness))
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
					
					for (Constraint constraint : root.constraintsRecursive)
					{
						valid = valid && constraint.port.get(timepoint);
					}
					
					if (valid)
					{
						validCount++;
						
						currentStates.add(current);
						
						current.save();
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
