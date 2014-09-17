package org.xtream.core.optimizer.beam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
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
	private Map<Constraint, Integer> constraintViolations = new HashMap<>();
	private int zeroOptionCount = 0;
	
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
	public Map<Constraint, Integer> getConstraintViolations()
	{
		return constraintViolations;
	}
	public int getZeroOptionCount()
	{
		return zeroOptionCount;
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
						
						for (Expression<?> expression : root.getCachingExpressions())
						{
							expression.get(current, timepoint);
						}
						
						// Check state validity
						
						boolean valid = true;
						
						for (Constraint constraint : root.getDescendantsByClass(Constraint.class))
						{
							valid = valid && constraint.getPort().get(current, timepoint);
							
							if (!constraint.getPort().get(current, timepoint))
							{
								if (!constraintViolations.containsKey(constraint))
								{
									constraintViolations.put(constraint, 0);
								}
								constraintViolations.put(constraint, constraintViolations.get(constraint) + 1);
							}
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
						// TODO Count per expression that fails!
						
						e.printStackTrace();
						
						zeroOptionCount++;
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
