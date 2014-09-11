package org.xtream.core.workbench.parts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.workbench.Part;

public class EngineMonitorPart<T extends Component> extends Part<T>
{

	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JFreeChart statesChart;
	private JFreeChart classesChart;
	private JFreeChart objectivesChart;
	private JFreeChart tracesChart;
	private JFreeChart memoryChart;
	private JFreeChart timeChart;
	private JFreeChart violationsChart;
	private JFreeChart optionsChart;
	
	private CategoryTableXYDataset states = new CategoryTableXYDataset();
	private CategoryTableXYDataset classes = new CategoryTableXYDataset();
	private CategoryTableXYDataset objectives = new CategoryTableXYDataset();
	private DefaultXYDataset traces = new DefaultXYDataset(); 
	private CategoryTableXYDataset memory = new CategoryTableXYDataset();
	private CategoryTableXYDataset time = new CategoryTableXYDataset();
	private CategoryTableXYDataset violations = new CategoryTableXYDataset();
	private CategoryTableXYDataset options = new CategoryTableXYDataset();
	
	public EngineMonitorPart()
	{
		this(0, 0);
	}
	public EngineMonitorPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public EngineMonitorPart(int x, int y, int width, int height)
	{
		super("Engine monitor", EngineMonitorPart.class.getClassLoader().getResource("parts/engine_monitor.png"), x, y, width, height);
		
		statesChart = ChartFactory.createXYLineChart("States", null, "Count", states, PlotOrientation.VERTICAL, true, true, false);
		classesChart = ChartFactory.createXYLineChart("Clusters", null, "Count", classes, PlotOrientation.VERTICAL, true, true, false);
		objectivesChart = ChartFactory.createXYLineChart("Objectives", null, "Objective", objectives, PlotOrientation.VERTICAL, true, true, false);
		tracesChart = ChartFactory.createXYLineChart("Traces", null, "Objective", traces, PlotOrientation.VERTICAL, false, true, false);
		memoryChart = ChartFactory.createXYLineChart("Memory", null, "Megabyte", memory, PlotOrientation.VERTICAL, true, true, false);
		timeChart = ChartFactory.createStackedXYAreaChart("Time", null, "Milliseconds", time, PlotOrientation.VERTICAL, true, true, false);
		violationsChart = ChartFactory.createXYLineChart("Constraint Violations", null, "Count", violations, PlotOrientation.VERTICAL, true, true, false);
		optionsChart = ChartFactory.createXYLineChart("Zero Options", null, "Count", options, PlotOrientation.VERTICAL, false, true, false);
		
		statesChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		statesChart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		statesChart.getXYPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		classesChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		
		objectivesChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		objectivesChart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		objectivesChart.getXYPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		memoryChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		memoryChart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		memoryChart.getXYPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		optionsChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		
		statesChart.setAntiAlias(true);
		classesChart.setAntiAlias(true);
		objectivesChart.setAntiAlias(true);
		tracesChart.setAntiAlias(true);
		memoryChart.setAntiAlias(true);
		timeChart.setAntiAlias(true);
		violationsChart.setAntiAlias(true);
		optionsChart.setAntiAlias(true);
		
		statesChart.setTextAntiAlias(true);
		classesChart.setTextAntiAlias(true);
		objectivesChart.setTextAntiAlias(true);
		tracesChart.setTextAntiAlias(true);
		memoryChart.setTextAntiAlias(true);
		timeChart.setTextAntiAlias(true);
		violationsChart.setTextAntiAlias(true);
		optionsChart.setTextAntiAlias(true);
		
		statesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		classesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		objectivesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		tracesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		memoryChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		timeChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		violationsChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		optionsChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		
		ChartPanel statesPanel = new ChartPanel(statesChart);
		ChartPanel classesPanel = new ChartPanel(classesChart);
		ChartPanel objectivesPanel = new ChartPanel(objectivesChart);
		ChartPanel tracesPanel = new ChartPanel(tracesChart);
		ChartPanel memoryPanel = new ChartPanel(memoryChart);
		ChartPanel timePanel = new ChartPanel(timeChart);
		ChartPanel violationsPanel = new ChartPanel(violationsChart);
		ChartPanel optionsPanel = new ChartPanel(optionsChart);
		
		GridLayout layout = new GridLayout(8, 1);
		
		JPanel panel = new JPanel(layout);
		panel.add(statesPanel);
		panel.add(classesPanel);
		panel.add(objectivesPanel);
		panel.add(tracesPanel);
		panel.add(memoryPanel);
		panel.add(timePanel);
		panel.add(violationsPanel);
		panel.add(optionsPanel);
		
		getPanel().add(panel);
	}
	
	@Override
	public void start()
	{
		super.start();
		
		states.clear();
		classes.clear();
		objectives.clear();
		
		traces = new DefaultXYDataset();
		tracesChart.getXYPlot().setDataset(traces);
		
		memory.clear();
		time.clear();
		violations.clear();
		options.clear();
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		super.handle(timepoint, statistics, equivalenceClasses, best);
		
		states.add(timepoint, statistics.generatedStates, "Generated");
		states.add(timepoint, statistics.validStates, "Valid");
		states.add(timepoint, statistics.preferredStates, "Preferred");
		
		classes.add(timepoint, equivalenceClasses.size(), "Count");
		
		objectives.add(timepoint, statistics.minObjective, "Minimum");
		objectives.add(timepoint, statistics.avgObjective, "Average");
		objectives.add(timepoint, statistics.maxObjective, "Maximum");
		
		// Draw lines
		
		if (best != null)
		{
			traces = new DefaultXYDataset();
			
			int series = 0;
			
			for (Entry<Key, List<State>> cluster : equivalenceClasses.entrySet())
			{
				State state = cluster.getValue().get(0);
						
				double[][] data = new double[2][timepoint + 1];
				
				for (int step = 0; step <= timepoint; step++)
				{
					data[0][step] = step;
					data[1][step] = getRoot().getDescendantByClass(Objective.class).getPort().get(state, step);
				}
				
				traces.addSeries(series, data);
				
				if (Math.abs(data[1][timepoint] - statistics.maxObjective) < Math.abs(data[1][timepoint] - statistics.avgObjective))
				{
					tracesChart.getXYPlot().getRenderer().setSeriesPaint(series, Color.GREEN);
				}
				else if (Math.abs(data[1][timepoint] - statistics.minObjective) < Math.abs(data[1][timepoint] - statistics.avgObjective))
				{
					tracesChart.getXYPlot().getRenderer().setSeriesPaint(series, Color.RED);
				}
				else
				{
					tracesChart.getXYPlot().getRenderer().setSeriesPaint(series, Color.BLUE);
				}
				
				tracesChart.getXYPlot().getRenderer().setSeriesStroke(series, new BasicStroke(STROKE));
				
				series++;
			}
		}
		
		tracesChart.getXYPlot().setDataset(traces);
		
		memory.add(timepoint, Memory.maxMemory(), "Maximum");
		memory.add(timepoint, Memory.totalMemory(), "Total");
		memory.add(timepoint, Memory.usedMemory(), "Used");
		
		time.add(timepoint, statistics.branch, "Branch");
		time.add(timepoint, statistics.norm, "Normalize");
		time.add(timepoint, statistics.cluster, "Cluster");
		time.add(timepoint, statistics.sort, "Sort");
		time.add(timepoint, statistics.stats, "Stats");
		
		for (Entry<Constraint, Integer> entry : statistics.violations.entrySet())
		{
			violations.add(timepoint, entry.getValue(), entry.getKey().getQualifiedName());
		}
		for (int i = 0; i < violations.getSeriesCount(); i++)
		{
			violationsChart.getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
		}
		
		options.add(timepoint, statistics.zeroOptionCount, "Zero Options");
	}

}
