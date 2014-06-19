package org.xtream.core.workbench;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.ui.ApplicationFrame;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Engine;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.utilities.monitors.CompositeMonitor;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.parts.AggregateGraphPart;
import org.xtream.core.workbench.parts.AnimationPart;
import org.xtream.core.workbench.parts.ArchitecturePart;
import org.xtream.core.workbench.parts.ChartPart;
import org.xtream.core.workbench.parts.MonitorPart;
import org.xtream.core.workbench.parts.TablePart;
import org.xtream.core.workbench.parts.TreePart;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

public class Workbench<T extends Component>
{
	
	private Engine<T> engine;
	private Bus<T> bus;
	private JSlider slider;
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching)
	{
		this(root, duration, samples, clusters, randomness, caching, new TreePart<T>(0,0,1,2), new TablePart<T>(1,0,2,1), new ArchitecturePart<T>(3,0,2,1), new AnimationPart<T>(1,1,2,1), new ChartPart<T>(3,1,2,1), new MonitorPart<T>(5,0,1,2));
	}
	
	public Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, Graph graph)
	{
		this(root, duration, samples, clusters, randomness, caching, new TreePart<T>(0,0,1,2), new TablePart<T>(1,0,2,1), new ArchitecturePart<T>(3,0,2,1), new AggregateGraphPart<T>(graph,1,1,2,1), new ChartPart<T>(3,1,2,1), new MonitorPart<T>(5,0,1,2));
	}
	
	@SafeVarargs
	private Workbench(T root, int duration, int samples, int clusters, double randomness, double caching, Part<T>... parts)
	{
		try
		{
			// Engine
			
			engine = new Engine<>(root, Runtime.getRuntime().availableProcessors() - 1);
			
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
			
			durationField.setEditable(false);
			samplesField.setEditable(false);
			clustersField.setEditable(false);
			randomnessField.setEditable(false);
			cachingField.setEditable(false);
			
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
						bus.trigger(new JumpEvent<T>(null, slider.getValue()));
					}
				}
			);
			
			JButton play = new JButton("Play");
			
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
			topbar.addSeparator();
			topbar.add(startButton);
			topbar.add(stopButton);
			topbar.addSeparator();
			topbar.add(new JLabel("Time"));
			topbar.add(timeBar);
			topbar.add(new JLabel("Memory"));
			topbar.add(memoryBar);
			
			JToolBar bottombar = new JToolBar("Toolbar");
			bottombar.setFloatable(false);
			bottombar.setLayout(new BorderLayout());
			bottombar.add(slider, BorderLayout.CENTER);
			bottombar.add(play, BorderLayout.LINE_START);
			
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
			
			JFrame frame = new ApplicationFrame("Xtream - Discrete-Time Dynamic Optimization Framework");
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
			
			allMonitor.add(new EngineMonitor<T>(timeBar, memoryBar, slider, duration));
			
			for (Monitor<T> monitor : parts)
			{
				allMonitor.add(monitor);
			}
			
			// run
			
			engine.run(duration, samples, clusters, randomness, allMonitor);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

}
