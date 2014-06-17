package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Component;
import org.xtream.core.model.expressions.CachingExpression;
import org.xtream.core.model.markers.Constraint;

public class Worker<T extends Component> implements Runnable
{
	private T root;
	private int timepoint;
	private int samples;
	private double randomness;
	private Map<Key, List<State>> previousGroups;
	private Queue<Key> queue;
	
	private int generatedCount = 0;
	private int validCount = 0;
	private List<State> currentStates = new ArrayList<>();
	
	public Worker(T root, int timepoint, int samples, double randomness, Map<Key, List<State>> previousGroups, Queue<Key> queue)
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
					
					try
					{
						// Create state object
						
						State current = new State(root, timepoint, previous);
						
						// Calculate caching expressions
						
						for (CachingExpression<?> expression : root.getDescendantsByClass(CachingExpression.class))
						{
							expression.evaluate(current, timepoint);
						}
						
						// Check state validity
						
						boolean valid = true;
						
						for (Constraint constraint : root.getDescendantsByClass(Constraint.class))
						{
							valid = valid && constraint.getPort().get(current, timepoint);
							
							// TODO Count and visualize
						}
						
						// Remember valid state
						
						if (valid)
						{
							validCount++;
							
							currentStates.add(current);
						}
					}
					catch (IllegalStateException e)
					{
						// No options in this state!
						
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
