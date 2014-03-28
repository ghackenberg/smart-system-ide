package org.xtream.core.workbench;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
import org.xtream.core.workbench.printers.SimpleMobilityGraphPrinter;
import org.xtream.core.workbench.printers.HistogramPrinter;
import org.xtream.core.workbench.printers.TablePrinter;
import org.xtream.core.workbench.viewers.GraphViewer;

public class Workbench<T extends Component>
{
	
	private Engine<T> engine;
	
	public Workbench(Class<T> type, int duration, int samples, int classes, double randomness)
	{
		engine = new Engine<>(type);
		
		try
		{
			// Look and feel
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
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
			
			// Tabs
			
			JTabbedPane tabs = new JTabbedPane();
			
			// Frame
			
			JFrame frame = new ApplicationFrame("Xtream - Rapid Prototyping Framework for Smart Systems (including Built-in Extensible Optimizer and Visualizer)");
			frame.setLayout(new BorderLayout());
			frame.add(toolbar, BorderLayout.PAGE_START);
			frame.add(tabs, BorderLayout.CENTER);
			frame.add(new JLabel("Copyright 2014, Smart Energy Systems Group, Chair for Software & Systems Engineering, Technische Universität München"), BorderLayout.PAGE_END);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			// Viewers
			
			Viewer<T> graphViewer = new GraphViewer<>(tabs);
			Viewer<T> allViewer = new CompositeViewer<>(graphViewer);
			
			// Monitors
			
			Monitor csvMonitor = new CSVMonitor(new PrintStream(new File("Monitor.csv")));
			Monitor chartMonitor = new ChartMonitor(tabs);
			Monitor progressMonitor = new ProgressMonitor(timeBar, memoryBar, duration);
			Monitor allMonitor = new CompositeMonitor(csvMonitor, chartMonitor, progressMonitor);
			
			// Printers
			
			Printer<T> csvPrinter = new CSVPrinter<>(new PrintStream(new File("Printer.csv")));
			Printer<T> chartPrinter = new ChartPrinter<>(tabs);
			Printer<T> tablePrinter = new TablePrinter<>(tabs);
			Printer<T> allPrinter = new CompositePrinter<>(csvPrinter, chartPrinter, tablePrinter);
			
			// run
			
			engine.run(duration, samples, classes, randomness, allViewer, allMonitor, allPrinter);
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
	
	public Workbench(Class<T> type, int duration, int samples, int classes, double randomness, Graph graph)
	{
		engine = new Engine<>(type);
		
		try
		{
			// Look and feel
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
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
			
			// Tabs
			
			JTabbedPane tabs = new JTabbedPane();
			
			// Frame
			
			JFrame frame = new ApplicationFrame("Xtream - Rapid Prototyping Framework for Smart Systems (including Built-in Extensible Optimizer and Visualizer)");
			frame.setLayout(new BorderLayout());
			frame.add(toolbar, BorderLayout.PAGE_START);
			frame.add(tabs, BorderLayout.CENTER);
			frame.add(new JLabel("Copyright 2014, Smart Energy Systems Group, Chair for Software & Systems Engineering, Technische Universität München"), BorderLayout.PAGE_END);
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			// Viewers
			
			Viewer<T> graphViewer = new GraphViewer<>(tabs);
			Viewer<T> allViewer = new CompositeViewer<>(graphViewer);
			
			// Monitors
			
			Monitor csvMonitor = new CSVMonitor(new PrintStream(new File("Monitor.csv")));
			Monitor chartMonitor = new ChartMonitor(tabs);
			Monitor progressMonitor = new ProgressMonitor(timeBar, memoryBar, duration);
			Monitor allMonitor = new CompositeMonitor(csvMonitor, chartMonitor, progressMonitor);
			
			// Printers
			
			Printer<T> csvPrinter = new CSVPrinter<>(new PrintStream(new File("Printer.csv")));
			Printer<T> chartPrinter = new ChartPrinter<>(tabs);
			Printer<T> tablePrinter = new TablePrinter<>(tabs);
			Printer<T> graphPrinter = new SimpleMobilityGraphPrinter<>(tabs, graph);
			Printer<T> histogramChartPrinter = new HistogramPrinter<>(tabs);
			Printer<T> allPrinter = new CompositePrinter<>(csvPrinter, chartPrinter, tablePrinter, graphPrinter, histogramChartPrinter);
			
			// run
			
			engine.run(duration, samples, classes, randomness, allViewer, allMonitor, allPrinter);
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

}
