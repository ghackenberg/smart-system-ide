package org.xtream.core.workbench;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.jfree.ui.ApplicationFrame;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Engine;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.optimizer.monitors.CSVMonitor;
import org.xtream.core.optimizer.monitors.CompositeMonitor;
import org.xtream.core.optimizer.printers.CSVPrinter;
import org.xtream.core.optimizer.printers.CompositePrinter;
import org.xtream.core.optimizer.viewers.CompositeViewer;
import org.xtream.core.workbench.monitors.ChartMonitor;
import org.xtream.core.workbench.monitors.ProgressMonitor;
import org.xtream.core.workbench.printers.ChartPrinter;
import org.xtream.core.workbench.printers.GraphPrinter;
import org.xtream.core.workbench.printers.HistogramPrinter;
import org.xtream.core.workbench.printers.LwjglAnimationPrinter;
import org.xtream.core.workbench.printers.TablePrinter;
import org.xtream.core.workbench.viewers.GraphViewer;
import org.xtream.core.workbench.viewers.TreeViewer;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

public class Workbench<T extends Component>
{
	
	private Engine<T> engine;
	
	public Workbench(Class<T> type, int duration, int samples, int classes, double randomness)
	{
		this(type, duration, samples, classes, randomness, new TreeViewer<>(0,0,1,2), new GraphViewer<>(1,0,2,1), /*new JmeAnimationPrinter<>(3,0,2,1), new PovrayAnimationPrinter<>(3,0,2,1),*/ new LwjglAnimationPrinter<>(3,0,2,1), new ChartPrinter<>(1,1,2,1), new TablePrinter<>(3,1,2,1), new ChartMonitor(5,0,1,2));
	}
	
	public Workbench(Class<T> type, int duration, int samples, int classes, double randomness, Graph graph)
	{
		this(type, duration, samples, classes, randomness, new TreeViewer<>(0,0,1,2), new GraphViewer<>(1,0,2,1), /*new JmeAnimationPrinter<>(3,0,2,1), new PovrayAnimationPrinter<>(3,0,2,1),*/ new LwjglAnimationPrinter<>(3,0,2,1), new GraphPrinter<>(graph,3,0,2,1), new ChartPrinter<>(1,1,2,1), new HistogramPrinter<>(3,1,2,1), new ChartMonitor(5,0,1,2));
	}
	
	@SuppressWarnings("unchecked")
	private Workbench(Class<T> type, int duration, int samples, int classes, double randomness, Part... parts)
	{
		engine = new Engine<>(type, Runtime.getRuntime().availableProcessors() - 1);
		
		try
		{	
			// Controls
			
			JTextField durationField = new JTextField("" + duration, 5);
			JTextField samplesField = new JTextField("" + samples, 5);
			JTextField classesField = new JTextField("" + classes, 5);
			JTextField randomnessField = new JTextField("" + randomness, 5);
			
			durationField.setEditable(false);
			samplesField.setEditable(false);
			classesField.setEditable(false);
			randomnessField.setEditable(false);
			
			JButton startButton = new JButton("Start");
			JButton stopButton = new JButton("Stop");
			
			startButton.setEnabled(false);
			stopButton.setEnabled(false);
			
			JProgressBar timeBar = new JProgressBar();
			JProgressBar memoryBar = new JProgressBar();
			
			timeBar.setStringPainted(true);
			memoryBar.setStringPainted(true);
			
			timeBar.setForeground(Color.GREEN);
			memoryBar.setForeground(Color.RED);
			
			// Toolbar
			
			JToolBar toolbar = new JToolBar("Toolbar");
			toolbar.setFloatable(false);
			toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
			toolbar.add(new JLabel("Duration"));
			toolbar.add(durationField);
			toolbar.add(new JLabel("Classes"));
			toolbar.add(classesField);
			toolbar.add(new JLabel("Samples"));
			toolbar.add(samplesField);
			toolbar.add(new JLabel("Randomness"));
			toolbar.add(randomnessField);
			toolbar.addSeparator();
			toolbar.add(startButton);
			toolbar.add(stopButton);
			toolbar.addSeparator();
			toolbar.add(new JLabel("Time"));
			toolbar.add(timeBar);
			toolbar.add(new JLabel("Memory"));
			toolbar.add(memoryBar);
			
			// Bus
			
			Bus bus = new Bus();
			
			for (Part part : parts)
			{
				part.connect(bus);
			}
			
			// Dock
			
			SplitDockGrid grid = new SplitDockGrid();
			for (Part part : parts)
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
			
			JFrame frame = new ApplicationFrame("Xtream - Discrete-Time Optimization Framework");
			frame.setLayout(new BorderLayout());
			frame.add(toolbar, BorderLayout.PAGE_START);
			frame.add(station.getComponent(), BorderLayout.CENTER);
			frame.add(new JLabel("Copyright 2014, Smart Energy Systems Group, Chair for Software & Systems Engineering, Technische Universität München"), BorderLayout.PAGE_END);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setIconImage(icon.getImage());
			
			// Viewers
			
			CompositeViewer<T> allViewer = new CompositeViewer<>();
			
			for (Part part : parts)
			{
				if (part instanceof Viewer)
				{
					allViewer.add((Viewer<T>) part);
				}
			}
			
			// Monitors
			
			CompositeMonitor allMonitor = new CompositeMonitor();
			
			allMonitor.add(new CSVMonitor(new PrintStream(new File("Monitor.csv"))));
			allMonitor.add(new ProgressMonitor(timeBar, memoryBar, duration));
			
			for (Part part : parts)
			{
				if (part instanceof Monitor)
				{
					allMonitor.add((Monitor) part);
				}
			}
			
			// Printers
			
			CompositePrinter<T> allPrinter = new CompositePrinter<>();
			
			allPrinter.add(new CSVPrinter<T>(new PrintStream(new File("Printer.csv"))));
			
			for (Part part : parts)
			{
				if (part instanceof Printer)
				{
					allPrinter.add((Printer<T>) part);
				}
			}
			
			// run
			
			engine.run(duration, samples, classes, randomness, allViewer, allMonitor, allPrinter);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

}
