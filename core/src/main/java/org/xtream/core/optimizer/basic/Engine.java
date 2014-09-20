package org.xtream.core.optimizer.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;

public class Engine<T extends Component> extends org.xtream.core.optimizer.Engine<T>
{
	
	public Engine(T root)
	{
		super(root);
	}
	
	@Override
	public State run(int duration, boolean prune, Monitor<T> monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Dummy objects
		
		Statistics statistics = new Statistics();
		
		statistics.violations = new HashMap<>();
		
		Key key = new Key();
		
		Map<Key, List<State>> clusters = new HashMap<>();
		
		clusters.put(key, new ArrayList<>());
		
		// Calculate one path
		
		State iterator = new State(root);
		
		for (int i = 0; i < duration; i++)
		{
			iterator = new State(root, i, iterator);
			
			for (Expression<?> expression : root.getCachingExpressions())
			{
				expression.get(iterator, i);
			}
			
			boolean valid = true;
			
			for (Constraint constraint : root.getDescendantsByClass(Constraint.class))
			{
				valid = valid && constraint.getPort().get(iterator, i);
			}
			
			if (valid)
			{
				/*double objective = */root.getDescendantByClass(Objective.class).getPort().get(iterator, i);
				
				if (root.getDescendantsByClass(MinObjective.class).size() == 1)
				{
					// smaller?
				}
				else if (root.getDescendantsByClass(MaxObjective.class).size() == 1)
				{
					// greater?
				}
				else
				{
					throw new IllegalStateException("No objective defined!");
				}
				
				clusters.get(key).clear();
				clusters.get(key).add(iterator);
				
				monitor.handle(i, statistics, clusters, iterator);
			}
			else
			{
				break;
			}
		}
		
		// Stop monitor
		
		monitor.stop();
		
		// Return best state
		
		return iterator;
	}

}
