package org.xtream.core.optimizer.beam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Equivalence;

public class Worker<T extends Component> implements Runnable
{
	private T root;
	private int timepoint;
	private int samples;
	private boolean prune;
	private long duration;
	private Map<Key, List<State>> previousGroups;
	private Queue<Key> queue;
	
	private int generatedCount = 0;
	private int validCount = 0;
	private List<State> currentStates = new ArrayList<>();
	private Map<Constraint, Integer> constraintViolations = new HashMap<>();
	private int zeroOptionCount = 0;
	
	public Worker(T root, int timepoint, int samples, boolean prune, long duration, Map<Key, List<State>> previousGroups, Queue<Key> queue)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.samples = samples;
		this.prune = prune;
		this.duration = duration;
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
				
				long deadline = System.currentTimeMillis() + duration;
				
				// Best state
				process(previousGroup.get(0));
				
				// Extreme states
				if (previousGroup.size() > 1)
				{
					List<Equivalence> equivalences = root.getDescendantsByClass(Equivalence.class);
					
					for (Equivalence equivalence : equivalences)
					{
						double min_value = equivalence.getPort().get(previousGroup.get(0), timepoint - 1);
						double max_value = equivalence.getPort().get(previousGroup.get(0), timepoint - 1);
						
						State min_state = previousGroup.get(0);
						State max_state = previousGroup.get(0);
						
						for (State state : previousGroup)
						{
							double value = equivalence.getPort().get(state, timepoint - 1);
							
							if (value < min_value)
							{
								min_value = value;
								min_state = state;
							}
							if (value > max_value)
							{
								max_value = value;
								max_state = state;
							}
						}
						
						process(min_state);
						process(max_state);
					}
					
					// Random states
					while (System.currentTimeMillis() < deadline)
					{
						int random = (int) Math.floor(1 + Math.random() * (previousGroup.size() - 1));
						
						process(previousGroup.get(random));
					}
				}
			}
		}
		catch (NoSuchElementException exception)
		{
			// Done!
		}
	}
	
	private void process(State previous)
	{
		for (int sample = 0; sample < samples; sample++)
		{
			generatedCount++;
			
			// Select Status
			
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
				else
				{
					//sample--;
					
					if (!prune)
					{
						currentStates.add(current);
					}
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
