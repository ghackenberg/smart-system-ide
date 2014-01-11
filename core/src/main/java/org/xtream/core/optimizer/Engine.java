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

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jfree.ui.ApplicationFrame;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.monitors.CMDMonitor;
import org.xtream.core.optimizer.monitors.CSVMonitor;
import org.xtream.core.optimizer.monitors.ChartMonitor;
import org.xtream.core.optimizer.monitors.CompositeMonitor;
import org.xtream.core.optimizer.printers.CMDPrinter;
import org.xtream.core.optimizer.printers.CSVPrinter;
import org.xtream.core.optimizer.printers.ChartPrinter;
import org.xtream.core.optimizer.printers.CompositePrinter;
import org.xtream.core.optimizer.printers.TablePrinter;

public class Engine<T extends Component>
{
	
	public Class<T> type;
	public int processors;
	public List<Thread> threads;
	public List<Worker> workers;
	public List<T> roots;
	public int timepoint;
	
	public Engine(Class<T> type)
	{
		this(type, Runtime.getRuntime().availableProcessors());
	}
	
	public Engine(Class<T> type, int processors)
	{
		this.type = type;
		this.processors = processors;
		
		threads = new ArrayList<>(processors);
		workers = new ArrayList<>(processors);
		roots = new ArrayList<>(processors);
		
		try
		{
			for (int i = 0; i < processors; i++)
			{
				roots.add(i, type.newInstance());
				roots.get(i).init();
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
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedLookAndFeelException e)
			{
				e.printStackTrace();
			}
			
			JTabbedPane tabs = new JTabbedPane();
			
			Monitor cmdMonitor = new CMDMonitor();
			Monitor csvMonitor = new CSVMonitor(new PrintStream(new File("Monitor.csv")));
			Monitor chartMonitor = new ChartMonitor(tabs);
			Monitor allMonitor = new CompositeMonitor(cmdMonitor, csvMonitor, chartMonitor);
			
			Printer<T> cmdPrinter = new CMDPrinter<>();
			Printer<T> csvPrinter = new CSVPrinter<>(new PrintStream(new File("Printer.csv")));
			Printer<T> chartPrinter = new ChartPrinter<>(tabs);
			Printer<T> tablePrinter = new TablePrinter<>(tabs);
			Printer<T> allPrinter = new CompositePrinter<>(cmdPrinter, csvPrinter, chartPrinter, tablePrinter);
			
			JFrame frame = new ApplicationFrame("Xtream - Rapid Prototyping Framework for Smart Systems (including Built-in Extensible Optimizer and Visualizer)");
			frame.add(tabs);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			run(duration, coverage, randomness, allMonitor, allPrinter);
		}
		catch (FileNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public void run(int duration, int coverage, double randomness, Monitor monitor, Printer<T> printer)
	{
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<Key, List<State>>();
		
		State start = new State(roots.get(0).portsRecursive.size(), roots.get(0).fieldsRecursive.size());
		
		start.connect(roots.get(0));
		start.save();
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(start);
		
		previousGroups.put(new Key(roots.get(0), -1), initialGroup);
		
		// Run optimization
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			SortedMap<Key, List<State>> currentGroups = Collections.synchronizedSortedMap(new TreeMap<Key, List<State>>());
			
			// Prepare statistics
			
			int generatedStates = 0;
			int validStates = 0;
			int dominantStates = 0;
			
			// Start threads
			
			Queue<Key> queue = new LinkedBlockingQueue<>(previousGroups.keySet());
			
			for (int proccessor = 0; proccessor < processors; proccessor++)
			{
				workers.add(proccessor, new Worker(roots.get(proccessor), timepoint, previousGroups.size(), coverage, randomness, previousGroups, currentGroups, queue));
				
				threads.add(proccessor, new Thread(workers.get(proccessor)));
				threads.get(proccessor).start();
			}
			
			// Join threads
			
			for (int processor = 0; processor < processors; processor++)
			{
				try
				{
					threads.get(processor).join();
					
					generatedStates += workers.get(processor).generatedCount;
					validStates += workers.get(processor).validCount;
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
				
				// Sort states
				
				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					Collections.sort(previousGroup.getValue());
					
					dominantStates += previousGroup.getValue().size();
				}
				
				// Calculate stats
				
				double minObjective = Double.MAX_VALUE;
				double avgObjective = 0;
				double maxObjective = Double.MIN_VALUE;

				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					for (Port<Double> port : roots.get(0).minObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double objective = state.get(port, timepoint);
							
							minObjective = Math.min(minObjective, objective);
							avgObjective += objective / dominantStates;
							maxObjective = Math.max(maxObjective, objective);
						}
					}
					for (Port<Double> port : roots.get(0).maxObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double objective = state.get(port, timepoint);
							
							minObjective = Math.min(minObjective, objective);
							avgObjective += objective / dominantStates;
							maxObjective = Math.max(maxObjective, objective);
						}
					}
				}
				
				// Print result
				
				monitor.handle(timepoint, generatedStates, validStates, dominantStates, minObjective, avgObjective, maxObjective, previousGroups);
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
			for (Port<Double> port : roots.get(0).minObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) > entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
			for (Port<Double> port : roots.get(0).maxObjectivesRecursive)
			{
				if (best.get(port, timepoint - 1) < entry.getValue().get(0).get(port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
		}
		
		best.restore(roots.get(0));
		
		// Stop monitor
		
		monitor.stop();
		
		// Print component
		
		printer.print(roots.get(0), timepoint);
	}

}
