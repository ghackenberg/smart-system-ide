package org.xtream.core.optimizer.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.core.model.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;

public class Engine<T extends Component> extends org.xtream.core.optimizer.Engine<T>
{
	
	private int samples;
	
	public Engine(T root, int samples)
	{
		super(root);
		this.samples = samples;
	}
	
	@Override
	public State run(int duration, boolean prune, Monitor<T> monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Dummy objects
		
		Statistics statistics = new Statistics();
		
		statistics.violations = new HashMap<>();
		
		statistics.minObjective = Double.MAX_VALUE;
		statistics.avgObjective = 0;
		statistics.maxObjective = Double.MIN_VALUE;
		
		Key key = new Key();
		
		Map<Key, List<State>> clusters = new HashMap<>();
		
		clusters.put(key, new ArrayList<State>());
		
		// Start search
		
		State best = this.follow(new State(root), null, 0, duration, key, clusters, monitor, statistics);
		
		// Stop monitor
		
		monitor.stop();
		
		System.out.println(best);
		
		return best;
	}
	
	public State follow(State previous, State best, int timepoint, int duration, Key key, Map<Key, List<State>> clusters, Monitor<T> monitor, Statistics statistics) 
	{
	   for (int i = 0; i < samples; i++)
	   {
		    State iterator = new State(root, timepoint, previous);
		    
			for (Expression<?> expression : root.getCachingExpressions()) 
			{
				expression.get(iterator, timepoint);
			}

			boolean valid = true;

			for (Constraint constraint : root.getDescendantsByClass(Constraint.class)) 
			{
				valid = valid && constraint.getPort().get(iterator, timepoint);
			}

			if (valid) 
			{
				clusters.get(key).clear();
				clusters.get(key).add(iterator);
			    
				if (best == null)
					monitor.handle(timepoint, statistics, clusters, iterator);
			    
				// Constrain execution to maximum depth
				if (timepoint+1 <= duration) 
				{
					State temp = this.follow(iterator, best, timepoint+1, duration, key, clusters, monitor, statistics);
					
					best = this.getBestState(temp, best, statistics);
				}
				else 
				{
					best = this.getBestState(iterator, best, statistics);
					
					if (best == iterator)
						monitor.handle(timepoint, statistics, clusters, best);
				}
			} 
	   }
	   
	   return best;
	}
	
	// Compare two states in terms of their objectives and return the best
	public State getBestState(State state, State other, Statistics statistics) 
	{	
		if (other == null) 
		{
			return state;
		}
		else if (state == null)
		{
			return other;
		}
		else 
		{
			State best;
			
			double currentObjectiveState = root.getDescendantByClass(Objective.class).getPort().get(state, state.getTimepoint());
			
			double currentObjectiveOther = root.getDescendantByClass(Objective.class).getPort().get(other, other.getTimepoint());
			
			if (root.getDescendantsByClass(MinObjective.class).size() == 1)
			{
				if (currentObjectiveState <= currentObjectiveOther) 
				{
					best = state;
					statistics.minObjective = Math.min(statistics.minObjective, currentObjectiveState);
				}
				else 
				{
					best = other;
					statistics.minObjective = Math.min(statistics.minObjective, currentObjectiveOther);
				}
				
			}
			else if (root.getDescendantsByClass(MaxObjective.class).size() == 1)
			{
				if (currentObjectiveState >= currentObjectiveOther) 
				{
					best = state;
					statistics.maxObjective = Math.max(statistics.maxObjective, currentObjectiveState);
				}
				else 
				{
					best = other;
					statistics.maxObjective = Math.max(statistics.maxObjective, currentObjectiveOther);
				}		
			}
			else
			{
				throw new IllegalStateException("No objective defined!");
			}				
			
			return best;
		}
	}
}
