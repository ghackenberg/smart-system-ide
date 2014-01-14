package org.xtream.core.optimizer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jfree.ui.ApplicationFrame;
import org.xtream.core.model.Component;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.optimizer.monitors.CMDMonitor;
import org.xtream.core.optimizer.monitors.CSVMonitor;
import org.xtream.core.optimizer.monitors.ChartMonitor;
import org.xtream.core.optimizer.monitors.CompositeMonitor;
import org.xtream.core.optimizer.printers.CSVPrinter;
import org.xtream.core.optimizer.printers.ChartPrinter;
import org.xtream.core.optimizer.printers.CompositePrinter;
import org.xtream.core.optimizer.printers.TablePrinter;
import org.xtream.core.optimizer.viewers.GraphViewer;

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
	
	public void run(int duration, int coverage, int classes, double randomness)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			JTabbedPane tabs = new JTabbedPane();
			
			Viewer<T> graphViewer = new GraphViewer<>(tabs);
			
			Monitor cmdMonitor = new CMDMonitor();
			Monitor csvMonitor = new CSVMonitor(new PrintStream(new File("Monitor.csv")));
			Monitor chartMonitor = new ChartMonitor(tabs);
			Monitor allMonitor = new CompositeMonitor(cmdMonitor, csvMonitor, chartMonitor);
			
			Printer<T> csvPrinter = new CSVPrinter<>(new PrintStream(new File("Printer.csv")));
			Printer<T> chartPrinter = new ChartPrinter<>(tabs);
			Printer<T> tablePrinter = new TablePrinter<>(tabs);
			Printer<T> allPrinter = new CompositePrinter<>(csvPrinter, chartPrinter, tablePrinter);
			
			JTextField durationField = new JTextField("" + duration, 5);
			JTextField coverageField = new JTextField("" + coverage, 5);
			JTextField classesField = new JTextField("" + classes, 5);
			JTextField randomnessField = new JTextField("" + randomness, 5);
			
			durationField.setEditable(false);
			coverageField.setEditable(false);
			classesField.setEditable(false);
			randomnessField.setEditable(false);
			
			JButton startButton = new JButton("Start");
			JButton stopButton = new JButton("Stop");
			
			startButton.setEnabled(false);
			stopButton.setEnabled(false);
			
			JToolBar toolbar = new JToolBar("Toolbar");
			toolbar.setFloatable(false);
			toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
			toolbar.add(new JLabel("Duration"));
			toolbar.add(durationField);
			toolbar.add(new JLabel("Coverage"));
			toolbar.add(coverageField);
			toolbar.add(new JLabel("Classes"));
			toolbar.add(classesField);
			toolbar.add(new JLabel("Randomness"));
			toolbar.add(randomnessField);
			toolbar.add(startButton);
			toolbar.add(stopButton);
			
			JFrame frame = new ApplicationFrame("Xtream - Rapid Prototyping Framework for Smart Systems (including Built-in Extensible Optimizer and Visualizer)");
			frame.setLayout(new BorderLayout());
			frame.add(toolbar, BorderLayout.PAGE_START);
			frame.add(tabs, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			run(duration, coverage, classes, randomness, graphViewer, allMonitor, allPrinter);
		}
		catch (FileNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
		catch (InstantiationException e)
		{
			throw new IllegalStateException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
		catch (UnsupportedLookAndFeelException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public void run(int duration, int coverage, int classes, double randomness, Viewer<T> viewer, Monitor monitor, Printer<T> printer)
	{
		viewer.view(roots.get(0));
		
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<Key, List<State>>();
		
		State start = new State(roots.get(0).portsRecursive.size(), roots.get(0).fieldsRecursive.size());
		
		start.connect(roots.get(0));
		start.save();
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(start);
		
		previousGroups.put(new Key(), initialGroup);
		
		// Run optimization
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			// Prepare statistics
			
			int generatedStates = 0;
			int validStates = 0;
			int dominantStates = 0;
			
			// Start threads
			
			Queue<Key> queue = new LinkedBlockingQueue<>(previousGroups.keySet());
			
			for (int proccessor = 0; proccessor < processors; proccessor++)
			{
				workers.add(proccessor, new Worker(roots.get(proccessor), timepoint, coverage, randomness, previousGroups, queue));
				
				threads.add(proccessor, new Thread(workers.get(proccessor)));
				threads.get(proccessor).start();
			}
			
			// All states
			
			List<State> currentStates = new LinkedList<>();
			
			// Join threads
			
			for (int processor = 0; processor < processors; processor++)
			{
				try
				{
					threads.get(processor).join();
					
					generatedStates += workers.get(processor).generatedCount;
					validStates += workers.get(processor).validCount;
					
					currentStates.addAll(workers.get(processor).currentStates);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			// Current groups
			
			SortedMap<Key, List<State>> currentGroups = new TreeMap<Key, List<State>>();
			
			// Calculate bounds
			
			double[] minEquivalences = new double[roots.get(0).equivalencesRecursive.size()];
			double[] maxEquivalences = new double[roots.get(0).equivalencesRecursive.size()];
			
			for (int i = 0; i < roots.get(0).equivalencesRecursive.size(); i++)
			{
				minEquivalences[i] = Double.MAX_VALUE;
				maxEquivalences[i] = Double.MIN_VALUE;
			}
			
			for (State current : currentStates)
			{
				for (int i = 0; i < roots.get(0).equivalencesRecursive.size(); i++)
				{
					minEquivalences[i] = Math.min(minEquivalences[i], current.get(roots.get(0).equivalencesRecursive.get(i).port, timepoint));
					maxEquivalences[i] = Math.max(maxEquivalences[i], current.get(roots.get(0).equivalencesRecursive.get(i).port, timepoint));
				}
			}
			
			// Sort groups
			
			for (State current : currentStates)
			{
				// Group Status
				
				Key currentKey = new Key(roots.get(0), current, minEquivalences, maxEquivalences, classes, timepoint);
				
				List<State> currentGroup = currentGroups.get(currentKey);
				
				if (currentGroup == null)
				{
					currentGroup = new LinkedList<State>();
					
					currentGroups.put(currentKey, currentGroup);
				}
				
				// Check Status
				
				boolean dominant = true;
				
				for (int index = 0; index < currentGroup.size(); index++)
				{
					State alternative = currentGroup.get(index);
					
					Integer difference = current.compareDominanceTo(alternative);
					
					if (difference != null)
					{
						if (difference < 0)
						{
							dominant = false;
							
							break; // do not keep
						}
						else if (difference == 0)
						{
							dominant = false;
							
							break; // do not keep
						}
						else if (difference > 0)
						{
							currentGroup.remove(index--);
							
							continue;
						}
						
						assert false;
					}
				}
				
				// Save Status
				
				if (dominant)
				{
					currentGroup.add(current);
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
					for (Objective objective : roots.get(0).minObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = state.get(objective.port, timepoint);
							
							minObjective = Math.min(minObjective, currentObjective);
							avgObjective += currentObjective / dominantStates;
							maxObjective = Math.max(maxObjective, currentObjective);
						}
					}
					for (Objective objective : roots.get(0).maxObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = state.get(objective.port, timepoint);
							
							minObjective = Math.min(minObjective, currentObjective);
							avgObjective += currentObjective / dominantStates;
							maxObjective = Math.max(maxObjective, currentObjective);
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
			for (Objective objective : roots.get(0).minObjectivesRecursive)
			{
				if (best.get(objective.port, timepoint - 1) > entry.getValue().get(0).get(objective.port, timepoint - 1))
				{
					best = entry.getValue().get(0);
				}
			}
			for (Objective objective : roots.get(0).maxObjectivesRecursive)
			{
				if (best.get(objective.port, timepoint - 1) < entry.getValue().get(0).get(objective.port, timepoint - 1))
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
