package org.xtream.core.workbench;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.DecimalFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
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
import org.xtream.core.utilities.visitors.PovrayVisitor;
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
	private static int counter = 0;
	private int speed = 1000;
	private int sliderMax;
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, true);
	}
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, boolean prune)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, prune, new ComponentHierarchyPart<T>(0,0,1,2), new ComponentChildrenPart<T>(0,2,1,2), new ComponentArchitecturePart<T>(1,0,2,2), new OptionChartMonitorPart<T>(3,0), new ViolationChartMonitorPart<T>(4,0), new ModelScenePart<T>(1,2,2,2), new ComponentChartsPart<T>(3,2,2,2), new StateChartMonitorPart<T>(5,0), new ClusterChartMonitorPart<T>(5,1), new TraceChartMonitorPart<T>(3,1), new StateSpacePart<T>(3,1), new ObjectiveChartMonitorPart<T>(4,1), new TimeChartMonitorPart<T>(5,2), new MemoryChartMonitorPart<T>(5,3));
	}
	
	@SafeVarargs
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, Part<T>... parts)
	{
		this(root, duration, samples, clusters, randomness, caching, rounds, true, parts);
	}
	
	@SafeVarargs
	public Workbench(final T root, int duration, int samples, int clusters, double randomness, double caching, int rounds, boolean prune, Part<T>... parts)
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
			
			final Icon iconPause = new ImageIcon(Workbench.class.getClassLoader().getResource("buttons/pause.png"));
			final Icon iconStart = new ImageIcon(Workbench.class.getClassLoader().getResource("buttons/play.png"));
			final Icon iconSave = new ImageIcon(Workbench.class.getClassLoader().getResource("buttons/save.png"));
			final Icon iconRepeat = new ImageIcon(Workbench.class.getClassLoader().getResource("buttons/repeat.png"));
			
			final JButton start = new JButton(iconStart);
			start.setEnabled(false);
			start.setPreferredSize(new Dimension(30,30));
			
			JButton export = new JButton(iconSave);
			export.setEnabled(false);
			export.setPreferredSize(new Dimension(30,30));
			
			final JToggleButton repeat = new JToggleButton(iconRepeat);
			repeat.setEnabled(false);
			repeat.setPreferredSize(new Dimension(30,30));
			
			SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
			final JSpinner rate = new JSpinner(model);
			rate.setPreferredSize(new Dimension(45,30));
			rate.setEnabled(false);
			
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
			
			JPanel panelWest = new JPanel(new FlowLayout());
			panelWest.add(start);
			panelWest.add(rate);
			panelWest.add(repeat);
			
			JPanel panelEast = new JPanel(new FlowLayout());
			panelEast.add(export);
			
			JToolBar bottombar = new JToolBar("Toolbar");
			bottombar.setFloatable(false);
			bottombar.setLayout(new BorderLayout());
			bottombar.add(panelWest, BorderLayout.WEST);
			bottombar.add(slider, BorderLayout.CENTER);
			bottombar.add(panelEast, BorderLayout.EAST);

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
			
			final State best = engine.run(duration, prune, allMonitor);
			sliderMax = slider.getMaximum();
			
			// print
			
			CSVPrinter<T> printer = new CSVPrinter<>(new PrintStream("Printer.csv"));
			
			printer.print(root, best, best.getTimepoint());
			
			// timer
			
			ActionListener listener = new ActionListener() 
			{			
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					
						counter = slider.getValue();
						counter += 1;
						if (counter <= sliderMax)
						{
							slider.setValue(counter);
						}
						else
						{
							if (repeat.isSelected() == false)
							{
								start.setIcon(iconStart);
							}
							else
							{
								slider.setValue(0);
								counter = 0;
							}
						}
				}
			};
			
			
			final Timer timer = new Timer(speed, listener);
			timer.setDelay(1000);
			
			ChangeListener changeLi = new ChangeListener() 
			{
				@Override
				public void stateChanged(ChangeEvent ce) 
				{
					timer.setDelay(1000/((int)rate.getValue()));
				}
			};
		
			// enable buttons
			
			start.setEnabled(true);
			start.addActionListener(new ActionListener() 
			{			
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if (start.getIcon() == iconStart)
					{
						if (slider.getValue() == sliderMax)
						{
							slider.setValue(0);
							timer.start();
							start.setIcon(iconPause);
						}
						else
						{
							timer.start();
							start.setIcon(iconPause);
						}
					}
					else
					{
						timer.stop();
						start.setIcon(iconStart);
					}
					
				}
			});
			
			repeat.setEnabled(true);
			
			export.setEnabled(true);
			export.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					try
					{
						JFileChooser exportFile = new JFileChooser();
						exportFile.setDialogTitle("Save");
						exportFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int ret = exportFile.showSaveDialog(null);
						if(ret == JFileChooser.APPROVE_OPTION)
						{						
							String path = exportFile.getSelectedFile().getAbsolutePath();
							System.out.println("Path" + path);
							String povPath = path + File.separator + "Movie.avs";
							
							FileWriter avi_writer = new FileWriter(povPath);
							
							for (int timepoint = 0; timepoint <= slider.getMaximum(); timepoint++)
							{	
								DecimalFormat df = new DecimalFormat("00");
								String pov = path + File.separator +"Frame_" + df.format(timepoint) + ".pov";
								FileWriter writer = new FileWriter(pov);
								new PovrayVisitor(writer, best, timepoint).handle(root);
								
								writer.close();
								
								// Render frame
								
								Process process = Runtime.getRuntime().exec("pvengine /EXIT /RENDER " + pov);
								process.waitFor();
							}
							
							avi_writer.write("ImageSource(\"" + path + File.separator + "Frame_%02d.png\", 00, 95," + (int)rate.getValue() +")");
							avi_writer.close();
						
							// Save movie as AVI File
							
							Runtime.getRuntime().exec("vdubmod");
						
						}
						else
						{
							exportFile.cancelSelection();
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});
			
			rate.setEnabled(true);
			rate.addChangeListener(changeLi);
			
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

}
