package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.markers.Constraint;

public class Worker implements Runnable
{
	private Component root;
	private int timepoint;
	private int samples;
	private double randomness;
	private Map<Key, List<State>> previousGroups;
	private Queue<Key> queue;
	
	private int generatedCount = 0;
	private int validCount = 0;
	private List<State> currentStates = new ArrayList<>();
	
	public Worker(Component root, int timepoint, int samples, double randomness, Map<Key, List<State>> previousGroups, Queue<Key> queue)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.samples = samples;
		this.randomness = randomness;
		this.previousGroups = previousGroups;
		this.queue = queue;
	}
	
	public int getGeneratedCount()
	{
		return generatedCount;
	}
	
	public int getValidCount()
	{
		return validCount;
	}
	
	public List<State> getCurrentStates()
	{
		return currentStates;
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
				
				for (int sample = 0; sample < samples; sample++)
				{
					generatedCount++;
					
					// Select Status
					
					State previous = previousGroup.get(0);
					
					if (sample > samples * (1 - randomness))
					{
						int random = (int) Math.floor(Math.random() * previousGroup.size());
						
						previous = previousGroup.get(random);
					}
					
					previous.restore(root);
					
					try
					{
						// Create Status
						
						State current = new State(root.getDescendantsByClass(Port.class).size(), timepoint, previous);
						
						current.connect(root);
						
						for (Port<?> port : root.getDescendantsByClass(Port.class))
						{
							port.get(timepoint);
						}
						
						// Check Status
						
						boolean valid = true;
						
						for (Constraint constraint : root.getDescendantsByClass(Constraint.class))
						{
							valid = valid && constraint.getPort().get(timepoint);
							
							//if (!constraint.port.get(timepoint))
							//{
							//	System.out.println(constraint.qualifiedName + " violated!");
							//}
							
							// TODO Count and visualize
						}
						
						if (valid)
						{
							validCount++;
							
							currentStates.add(current);
						}
					}
					catch (IllegalStateException e)
					{
						// No options in this state!
						
						//e.printStackTrace();
						
						// TODO Count and visualize
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
