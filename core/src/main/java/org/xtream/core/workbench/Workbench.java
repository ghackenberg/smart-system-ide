package org.xtream.core.workbench;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.ui.ApplicationFrame;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Engine;
import org.xtream.core.optimizer.beam.strategies.KMeansStrategy;
import org.xtream.core.utilities.monitors.CMDMonitor;
import org.xtream.core.utilities.monitors.CSVMonitor;
import org.xtream.core.utilities.monitors.CompositeMonitor;
import org.xtream.core.utilities.printers.CSVPrinter;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.parts.ComponentArchitecturePart;
import org.xtream.core.workbench.parts.ComponentChartsPart;
import org.xtream.core.workbench.parts.ComponentChildrenPart;
import org.xtream.core.workbench.parts.ComponentHierarchyPart;
import org.xtream.core.workbench.parts.ModelScenePart;
import org.xtream.core.workbench.parts.StateSpacePart;
import org.xtream.core.workbench.parts.charts.ClusterChartMonitorPart;
import org.xtream.core.workbench.parts.charts.MemoryChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ObjectiveChartMonitorPart;
import org.xtream.core.workbench.parts.charts.OptionChartMonitorPart;
import org.xtream.core.workbench.parts.charts.StateChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TimeChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TraceChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ViolationChartMonitorPart;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

public class Workbench<T extends Component>
{
	
	private static String TITLE = "XTREAM - Dynamic Optimization Framework";
	
	private Engine<T> engine;
	private Bus<T> bus;
	private JSlider slider;
	private int timepoint = -1;
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, true);
	}
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, boolean prune)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, prune, new ComponentHierarchyPart<T>(0,0,1,2), new ComponentChildrenPart<T>(0,2,1,2), new ComponentArchitecturePart<T>(1,0,2,2), new OptionChartMonitorPart<T>(3,0), new ViolationChartMonitorPart<T>(4,0), new ModelScenePart<T>(1,2,2,2), new StateSpacePart<T>(1,2,2,2), new ComponentChartsPart<T>(3,2,2,2), new StateChartMonitorPart<T>(5,0), new ClusterChartMonitorPart<T>(5,1), new TraceChartMonitorPart<T>(3,1), new ObjectiveChartMonitorPart<T>(4,1), new TimeChartMonitorPart<T>(5,2), new MemoryChartMonitorPart<T>(5,3));
	}
	
	@SafeVarargs
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, Part<T>... parts)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, true, parts);
	}
	
	@SafeVarargs
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, boolean prune, Part<T>... parts)
	{
		try
		{
			// Engine
			
			//engine = new org.xtream.core.optimizer.basic.Engine<T>(root);
			engine = new org.xtream.core.optimizer.beam.Engine<>(root, samples, clusters, randomness, Runtime.getRuntime().availableProcessors() - 1, new KMeansStrategy(rounds));
			
			// Bus
			
			bus = new Bus<T>();
			for (Part<T> part : parts)
			{
				part.setBus(bus);
			}
			
			// Root
			
			root.init(caching);
			for (Part<T> part : parts)
			{
				part.setRoot(root);
			}
			
			// Controls
			
			JTextField durationField = new JTextField("" + duration, 5);
			JTextField samplesField = new JTextField("" + samples, 5);
			JTextField clustersField = new JTextField("" + clusters, 5);
			JTextField randomnessField = new JTextField("" + randomness, 5);
			JTextField cachingField = new JTextField("" + caching, 5);
			JTextField roundsField = new JTextField("" + rounds, 5);
			
			durationField.setEditable(false);
			samplesField.setEditable(false);
			clustersField.setEditable(false);
			randomnessField.setEditable(false);
			cachingField.setEditable(false);
			roundsField.setEditable(false);
			
			JProgressBar timeBar = new JProgressBar();
			JProgressBar memoryBar = new JProgressBar();
			
			timeBar.setStringPainted(true);
			memoryBar.setStringPainted(true);
			
			timeBar.setForeground(Color.GREEN);
			memoryBar.setForeground(Color.RED);
			
			slider = new JSlider(0, 0, 0);
			slider.setMajorTickSpacing(10);
			slider.setMinorTickSpacing(1);
			slider.setPaintLabels(true);
			slider.setPaintTicks(true);
			slider.addChangeListener(new ChangeListener()
				{		
					@Override
					public void stateChanged(ChangeEvent event)
					{
						if (slider.getValue() != timepoint)
						{
							timepoint = slider.getValue();
							
							bus.broadcast(new JumpEvent<T>(null, timepoint));
						}
					}
				}
			);
			
			// Toolbar
			
			JToolBar topbar = new JToolBar("Toolbar");
			topbar.setFloatable(false);
			topbar.setLayout(new FlowLayout(FlowLayout.LEFT));
			topbar.add(new JLabel("Duration"));
			topbar.add(durationField);
			topbar.add(new JLabel("Clusters"));
			topbar.add(clustersField);
			topbar.add(new JLabel("Samples"));
			topbar.add(samplesField);
			topbar.add(new JLabel("Randomness"));
			topbar.add(randomnessField);
			topbar.add(new JLabel("Caching"));
			topbar.add(cachingField);
			topbar.add(new JLabel("Rounds"));
			topbar.add(roundsField);
			topbar.addSeparator();
			topbar.add(new JLabel("Time"));
			topbar.add(timeBar);
			topbar.add(new JLabel("Memory"));
			topbar.add(memoryBar);
			
			JToolBar bottombar = new JToolBar("Toolbar");
			bottombar.setFloatable(false);
			bottombar.setLayout(new BorderLayout());
			bottombar.add(slider, BorderLayout.CENTER);
			
			// Dock
			
			SplitDockGrid grid = new SplitDockGrid();
			for (Part<T> part : parts)
			{
				grid.addDockable(part.getX(), part.getY(), part.getWidth(), part.getHeight(), new DefaultDockable(part.getPanel(), part.getTitle(), part.getIcon()));
			}

			SplitDockStation station = new SplitDockStation();
			station.dropTree(grid.toTree());
			
			DockController controller = new DockController();
			controller.setTheme(new EclipseTheme());
			controller.add(station);
			
			// Frame
			
			ImageIcon icon = new ImageIcon(Workbench.class.getClassLoader().getResource("xtream.png"));
			
			JFrame frame = new ApplicationFrame(TITLE);
			frame.setLayout(new BorderLayout());
			frame.add(topbar, BorderLayout.PAGE_START);
			frame.add(station.getComponent(), BorderLayout.CENTER);
			frame.add(bottombar, BorderLayout.PAGE_END);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setIconImage(icon.getImage());
			
			// Monitors
			
			CompositeMonitor<T> allMonitor = new CompositeMonitor<>();
			
			allMonitor.add(new CMDMonitor<T>());
			allMonitor.add(new CSVMonitor<T>(new PrintStream("Monitor.csv")));
			allMonitor.add(new EngineMonitor<T>(timeBar, memoryBar, slider, duration));
			
			for (Monitor<T> monitor : parts)
			{
				allMonitor.add(monitor);
			}
			
			// run
			
			State best = engine.run(duration, prune, allMonitor);
			
			// print
			
			CSVPrinter<T> printer = new CSVPrinter<>(new PrintStream("Printer.csv"));
			
			printer.print(root, best, best.getTimepoint());
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

}
