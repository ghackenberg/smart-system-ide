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

import org.xtream.core.model.Component;
import org.xtream.core.model.State;
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
	private int samples;
	private int clusters;
	private int rounds;
	private double randomness;
	private Strategy strategy;
	
	public Engine(T root, int samples, int clusters, int rounds, double randomness)
	{
		this(root, samples, clusters, rounds, randomness, Runtime.getRuntime().availableProcessors());
	}
	public Engine(T root, int samples, int classes, int rounds, double randomness, int processors)
	{
		this(root, samples, classes, rounds, randomness, processors, new RandomStrategy());
	}
	public Engine(T root, int samples, int classes, int rounds, double randomness, int processors, Strategy strategy)
	{
		super(root);
		
		this.samples = samples;
		this.clusters = classes;
		this.rounds = rounds;
		this.randomness = randomness;
		this.processors = processors;
		this.strategy = strategy;
		
		threads = new ArrayList<>(processors);
		workers = new ArrayList<>(processors);
	}
	
	@Override
	public State run(int duration, boolean prune, Monitor<T> monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> clusters = new TreeMap<>();
		
		State best = new State(root);
		
		List<State> cluster = new ArrayList<>();
		
		cluster.add(best);
		
		clusters.put(new Key(), cluster);
		
		// Run optimization
		
		best = run(prune, 0, duration, best, clusters, monitor);
		
		// Stop monitor
		
		monitor.stop();
		
		// Return best state
		
		return best;
	}
	
	private State run(boolean prune, int timepoint, int duration, State best, SortedMap<Key, List<State>> previousClusters, Monitor<T> monitor)
	{
		if (timepoint < duration)
		{
			for (int round = 0; round < (Math.random() < (1. / (best.getTimepoint() + 2.) + Math.pow(1. - (timepoint * 1.) / (duration * 1.), best.getTimepoint() + 2.)) ? rounds : 1); round++)
			{
				// Prepare statistics
				
				Statistics statistics = new Statistics();
				
				statistics.generatedStates = 0;
				statistics.validStates = 0;
				statistics.preferredStates = 0;
				
				// Start threads
				
				statistics.branch = System.currentTimeMillis();
				
				Queue<Key> queue = new LinkedBlockingQueue<>(previousClusters.keySet());
				
				for (int proccessor = 0; proccessor < processors; proccessor++)
				{
					workers.add(proccessor, new Worker<T>(root, timepoint, samples, randomness, prune, previousClusters, queue));
					
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
				
				statistics.norm = System.currentTimeMillis() - statistics.norm;
				
				// Sort groups
				
				// Factorize cluster strategy
				
				statistics.cluster = System.currentTimeMillis();
				
				SortedMap<Key, List<State>> currentClusters = strategy.execute(currentStates, minEquivalences, maxEquivalences, clusters, timepoint, root);
				
				statistics.cluster = System.currentTimeMillis() - statistics.cluster;
				
				// Prepare next iteration
				
				if (currentClusters.size() > 0)
				{
					statistics.sort = System.currentTimeMillis();
					
					// Sort states
					
					for (Entry<Key, List<State>> currentCluster : currentClusters.entrySet())
					{
						Collections.sort(currentCluster.getValue(), new Comparator());
						
						statistics.preferredStates += currentCluster.getValue().size();
					}
					
					statistics.sort = System.currentTimeMillis() - statistics.sort;
					
					// Calculate stats
					
					statistics.stats = System.currentTimeMillis();
					
					statistics.minObjective = Double.MAX_VALUE;
					statistics.avgObjective = 0;
					statistics.maxObjective = Double.MIN_VALUE;
	
					for (Entry<Key, List<State>> currentCluster : currentClusters.entrySet())
					{
						for (Objective objective : root.getDescendantsByClass(MinObjective.class))
						{
							for (State state : currentCluster.getValue())
							{
								double currentObjective = objective.getPort().get(state, timepoint);
								
								statistics.minObjective = Math.min(statistics.minObjective, currentObjective);
								statistics.avgObjective += currentObjective / statistics.preferredStates;
								statistics.maxObjective = Math.max(statistics.maxObjective, currentObjective);
							}
						}
						for (Objective objective : root.getDescendantsByClass(MaxObjective.class))
						{
							for (State state : currentCluster.getValue())
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
					
					if (best.getTimepoint() <= timepoint)
					{
						for (Entry<Key, List<State>> entry : currentClusters.entrySet())
						{
							for (Objective objective : root.getDescendantsByClass(MinObjective.class))
							{
								if (best.getTimepoint() < timepoint || objective.getPort().get(best, timepoint) > objective.getPort().get(entry.getValue().get(0), timepoint))
								{
									best = entry.getValue().get(0);
								}
							}
							for (Objective objective : root.getDescendantsByClass(MaxObjective.class))
							{
								if (best.getTimepoint() < timepoint || objective.getPort().get(best, timepoint) < objective.getPort().get(entry.getValue().get(0), timepoint))
								{
									best = entry.getValue().get(0);
								}
							}
						}
					}
					
					// Print result
					
					monitor.handle(timepoint, statistics, currentClusters, best);
					
					// Follow clusters
					
					best = run(prune, timepoint + 1, duration, best, currentClusters, monitor);
				}
				else
				{
					statistics.sort = 0;
					statistics.stats = 0;
					statistics.minObjective = 0;
					statistics.maxObjective = 0;
					statistics.avgObjective = 0;
					
					monitor.handle(timepoint, statistics, currentClusters, best);
				}
			}
		}
		return best;
	}

}
