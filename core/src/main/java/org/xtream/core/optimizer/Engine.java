package org.xtream.core.optimizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.monitors.CSVMonitor;

public class Engine
{
	
	public Class<? extends Component> type;
	public int processors;
	public Thread[] threads;
	public Worker[] workers;
	public Component[] roots;
	public int timepoint;
	
	public Engine(Class<? extends Component> type)
	{
		this(type, Runtime.getRuntime().availableProcessors());
	}
	
	public Engine(Class<? extends Component> type, int processors)
	{
		this.type = type;
		this.processors = processors;
		this.threads = new Thread[processors];
		this.workers = new Worker[processors];
		this.roots = new Component[processors];
		
		try
		{
			for (int i = 0; i < processors; i++)
			{
				this.roots[i] = type.newInstance();
				this.roots[i].init();
			}
		}
		catch (InstantiationException e)
		{
			throw new IllegalStateException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public void run(int duration, int coverage, double randomness)
	{
		try
		{
			run(duration, coverage, randomness, new CSVMonitor(new PrintStream(new File("Monitor.csv"))));
		}
		catch (FileNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public void run(int duration, int coverage, double randomness, Monitor monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<Key, List<State>>();
		
		State start = new State(roots[0].portsRecursive.size(), roots[0].fieldsRecursive.size());
		
		start.connect(roots[0]);
		start.save();
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(start);
		
		previousGroups.put(new Key(roots[0], -1), initialGroup);
		
		// Run optimization
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			SortedMap<Key, List<State>> currentGroups = Collections.synchronizedSortedMap(new TreeMap<Key, List<State>>());
			
			// Prepare statistics
			
			int generatedCount = 0;
			int validCount = 0;
			int dominantCount = 0;
			
			// Start threads
			
			Queue<Key> queue = new LinkedBlockingQueue<>(previousGroups.keySet());
			
			for (int i = 0; i < processors; i++)
			{
				workers[i] = new Worker(roots[i], timepoint, previousGroups.size(), coverage, randomness, previousGroups, currentGroups, queue);
				
				threads[i] = new Thread(workers[i]);
				threads[i].start();
			}
			
			// Join threads
			
			for (int i = 0; i < processors; i++)
			{
				try
				{
					threads[i].join();
					
					generatedCount += workers[i].generatedCount;
					validCount += workers[i].validCount;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			// Prepare next iteration
			
			if (currentGroups.size() > 0)
			{
				previousGroups = new TreeMap<>(currentGroups);
				
				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					Collections.sort(previousGroup.getValue());
					
					dominantCount += previousGroup.getValue().size();
				}
				
				// Print result
				
				monitor.handle(timepoint, generatedCount, validCount, dominantCount, previousGroups.size());
			}
			else
			{
				break; // Stop optimization
			}
		}
		
		// Select best
		
		State best = previousGroups.get(previousGroups.firstKey()).get(0);
		
		for (Entry<Key, List<State>> entry : previousGroups.entrySet())
		{
			for (Port<Double> port : roots[0].minObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) > entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
			for (Port<Double> port : roots[0].maxObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) < entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
		}
		
		best.restore(roots[0]);
		
		// Stop monitor
		
		monitor.stop();
		
		// Print best
		
		System.out.print("Port");
		
		for (int i = 0; i < timepoint; i++)
		{
			System.out.print(";Timepoint " + i);
		}
		
		System.out.println();
		
		for (Port<?> port : roots[0].portsRecursive)
		{
			System.out.print(port.name);
			
			for (int i = 0; i < timepoint; i++)
			{
				System.out.print(";" + port.get(i));
			}
			
			System.out.println();
		}
	}

}
