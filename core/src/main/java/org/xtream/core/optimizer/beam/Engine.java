package org.xtream.core.optimizer.beam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.strategies.RandomStrategy;

public class Engine<T extends Component> extends org.xtream.core.optimizer.Engine<T>
{
	
	private int processors;
	private List<Thread> threads;
	private List<Worker<T>> workers;
	private int timepoint;
	private int samples;
	private int classes;
	private double randomness;
	private Strategy strategy;
	
	public Engine(T root, int samples, int classes, double randomness)
	{
		this(root, samples, classes, classes, Runtime.getRuntime().availableProcessors());
	}
	public Engine(T root, int samples, int classes, double randomness, int processors)
	{
		this(root, samples, classes, randomness, processors, new RandomStrategy());
	}
	public Engine(T root, int samples, int classes, double randomness, int processors, Strategy strategy)
	{
		super(root);
		
		this.samples = samples;
		this.classes = classes;
		this.randomness = randomness;
		this.processors = processors;
		this.strategy = strategy;
		
		threads = new ArrayList<>(processors);
		workers = new ArrayList<>(processors);
	}
	
	@Override
	public State run(int duration, Monitor<T> monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<>();
		
		State best = new State(root);
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(best);
		
		previousGroups.put(new Key(), initialGroup);
		
		// Run optimization
		
		Statistics statistics = new Statistics();
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			// Prepare statistics
			
			statistics.generatedStates = 0;
			statistics.validStates = 0;
			statistics.preferredStates = 0;
			
			// Start threads
			
			statistics.branch = System.currentTimeMillis();
			
			Queue<Key> queue = new LinkedBlockingQueue<>(previousGroups.keySet());
			
			for (int proccessor = 0; proccessor < processors; proccessor++)
			{
				workers.add(proccessor, new Worker<T>(root, timepoint, samples, randomness, previousGroups, queue));
				
				threads.add(proccessor, new Thread(workers.get(proccessor)));
				threads.get(proccessor).start();
			}
			
			// All states
			
			List<State> currentStates = new LinkedList<>();
			statistics.violations = new HashMap<>();
			statistics.zeroOptionCount = 0;
			
			// Join threads
			
			for (int processor = 0; processor < processors; processor++)
			{
				try
				{
					threads.get(processor).join();
					
					statistics.generatedStates += workers.get(processor).getGeneratedCount();
					statistics.validStates += workers.get(processor).getValidCount();
					
					currentStates.addAll(workers.get(processor).getCurrentStates());
					
					for (Entry<Constraint, Integer> entry : workers.get(processor).getConstraintViolations().entrySet())
					{
						if (!statistics.violations.containsKey(entry.getKey()))
						{
							statistics.violations.put(entry.getKey(), 0);
						}
						statistics.violations.put(entry.getKey(), statistics.violations.get(entry.getKey()) + entry.getValue());
					}
					statistics.zeroOptionCount += workers.get(processor).getZeroOptionCount();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			statistics.branch = System.currentTimeMillis() - statistics.branch;
			
			// Calculate bounds
			
			statistics.norm = System.currentTimeMillis();
			
			double[] minEquivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
			double[] maxEquivalences = new double[root.getDescendantsByClass(Equivalence.class).size()];
			
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				minEquivalences[i] = Double.MAX_VALUE;
				maxEquivalences[i] = Double.MIN_VALUE;
			}
			
			for (State current : currentStates)
			{
				for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
				{
					minEquivalences[i] = Math.min(minEquivalences[i], root.getDescendantsByClass(Equivalence.class).get(i).getPort().get(current, timepoint));
					maxEquivalences[i] = Math.max(maxEquivalences[i], root.getDescendantsByClass(Equivalence.class).get(i).getPort().get(current, timepoint));
				}
			}
			
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				if (minEquivalences[i] == maxEquivalences[i])
				{
					minEquivalences[i] = 0.;
					maxEquivalences[i] = 1.;
				}
			}
			
			// Print result of boundary calculation
			/*
			for (int i = 0; i < root.getDescendantsByClass(Equivalence.class).size(); i++)
			{
				System.out.println(root.getDescendantsByClass(Equivalence.class).get(i).getQualifiedName() + ": " + minEquivalences[i] + " - " + maxEquivalences[i]);
			}
			*/
			
			statistics.norm = System.currentTimeMillis() - statistics.norm;
			
			// Sort groups
			
			// Factorize cluster strategy
			
			statistics.cluster = System.currentTimeMillis();
			
			SortedMap<Key, List<State>> currentGroups = strategy.execute(currentStates, minEquivalences, maxEquivalences, classes, timepoint, root);
			
			statistics.cluster = System.currentTimeMillis() - statistics.cluster;
			
			// Prepare next iteration
			
			if (currentGroups.size() > 0)
			{
				statistics.sort = System.currentTimeMillis();
				
				previousGroups = new TreeMap<>(currentGroups);
				
				// Sort states
				
				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					Collections.sort(previousGroup.getValue(), new Comparator());
					
					statistics.preferredStates += previousGroup.getValue().size();
				}
				
				statistics.sort = System.currentTimeMillis() - statistics.sort;
				
				// Calculate stats
				
				statistics.stats = System.currentTimeMillis();
				
				statistics.minObjective = Double.MAX_VALUE;
				statistics.avgObjective = 0;
				statistics.maxObjective = Double.MIN_VALUE;

				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					for (Objective objective : root.getDescendantsByClass(MinObjective.class))
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = objective.getPort().get(state, timepoint);
							
							statistics.minObjective = Math.min(statistics.minObjective, currentObjective);
							statistics.avgObjective += currentObjective / statistics.preferredStates;
							statistics.maxObjective = Math.max(statistics.maxObjective, currentObjective);
						}
					}
					for (Objective objective : root.getDescendantsByClass(MaxObjective.class))
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = objective.getPort().get(state, timepoint);
							
							statistics.minObjective = Math.min(statistics.minObjective, currentObjective);
							statistics.avgObjective += currentObjective / statistics.preferredStates;
							statistics.maxObjective = Math.max(statistics.maxObjective, currentObjective);
						}
					}
				}
				
				statistics.stats = System.currentTimeMillis() - statistics.stats;
				
				// Select best
				
				best = previousGroups.get(previousGroups.firstKey()).get(0);
				
				for (Entry<Key, List<State>> entry : previousGroups.entrySet())
				{
					for (Objective objective : root.getDescendantsByClass(MinObjective.class))
					{
						if (objective.getPort().get(best, timepoint) > objective.getPort().get(entry.getValue().get(0), timepoint))
						{
							best = entry.getValue().get(0);
						}
					}
					for (Objective objective : root.getDescendantsByClass(MaxObjective.class))
					{
						if (objective.getPort().get(best, timepoint) < objective.getPort().get(entry.getValue().get(0), timepoint))
						{
							best = entry.getValue().get(0);
						}
					}
				}
				
				// Print result
				
				monitor.handle(timepoint, statistics, previousGroups, best);
			}
			else
			{
				statistics.sort = 0;
				statistics.stats = 0;
				statistics.minObjective = 0;
				statistics.maxObjective = 0;
				statistics.avgObjective = 0;
				
				monitor.handle(timepoint, statistics, currentGroups, null);
				
				break; // Stop optimization
			}
		}
		
		// Stop monitor
		
		monitor.stop();
		
		// Return best state
		
		return best;
	}

}
