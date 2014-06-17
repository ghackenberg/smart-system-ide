package org.xtream.core.workbench.parts;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.workbench.Part;

public class MonitorPart<T extends Component> extends Part<T>
{

	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private DefaultCategoryDataset states = new DefaultCategoryDataset();
	private DefaultCategoryDataset classes = new DefaultCategoryDataset();
	private DefaultCategoryDataset objectives = new DefaultCategoryDataset();
	private DefaultCategoryDataset memory = new DefaultCategoryDataset();
	private DefaultCategoryDataset time = new DefaultCategoryDataset();
	
	public MonitorPart()
	{
		this(0, 0);
	}
	public MonitorPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public MonitorPart(int x, int y, int width, int height)
	{
		super("Engine monitor", x, y, width, height);
		
		JFreeChart statesChart = ChartFactory.createLineChart("Number of generated, valid and dominant states", null, "States", states, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart classesChart = ChartFactory.createLineChart("Number of equivalence classes", null, "Clusters", classes, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart objectivesChart = ChartFactory.createLineChart("Mininum, average and maximum objective", null, "Costs", objectives, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart memoryChart = ChartFactory.createLineChart("Maximum, total and free memory", null, "Memory (in MB)", memory, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart timeChart = ChartFactory.createStackedAreaChart("Branch, norm, cluster, sort and stats time", null, "Time (in ms)", time, PlotOrientation.VERTICAL, true, true, false);
		
		statesChart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		statesChart.getCategoryPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		statesChart.getCategoryPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		classesChart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		
		objectivesChart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		objectivesChart.getCategoryPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		objectivesChart.getCategoryPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		memoryChart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		memoryChart.getCategoryPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		memoryChart.getCategoryPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
		
		statesChart.setTitle((String) null);
		classesChart.setTitle((String) null);
		objectivesChart.setTitle((String) null);
		memoryChart.setTitle((String) null);
		timeChart.setTitle((String) null);
		
		statesChart.setAntiAlias(true);
		classesChart.setAntiAlias(true);
		objectivesChart.setAntiAlias(true);
		memoryChart.setAntiAlias(true);
		timeChart.setAntiAlias(true);
		
		statesChart.setTextAntiAlias(true);
		classesChart.setTextAntiAlias(true);
		objectivesChart.setTextAntiAlias(true);
		memoryChart.setTextAntiAlias(true);
		timeChart.setTextAntiAlias(true);
		
		statesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		classesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		objectivesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		memoryChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		timeChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		
		ChartPanel statesPanel = new ChartPanel(statesChart);
		ChartPanel classesPanel = new ChartPanel(classesChart);
		ChartPanel objectivesPanel = new ChartPanel(objectivesChart);
		ChartPanel memoryPanel = new ChartPanel(memoryChart);
		ChartPanel timePanel = new ChartPanel(timeChart);
		
		GridLayout layout = new GridLayout(5, 1);
		
		JPanel panel = new JPanel(layout);
		panel.add(statesPanel);
		panel.add(classesPanel);
		panel.add(objectivesPanel);
		panel.add(memoryPanel);
		panel.add(timePanel);
		
		getPanel().add(panel);
	}
	
	@Override
	public void start()
	{
		states.clear();
		classes.clear();
		objectives.clear();
		memory.clear();
		time.clear();
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		states.addValue(statistics.generatedStates, "Generated", "" + timepoint);
		states.addValue(statistics.validStates, "Valid", "" + timepoint);
		states.addValue(statistics.dominantStates, "Preferred", "" + timepoint);
		
		classes.addValue(equivalenceClasses.size(), "Count", "" + timepoint);
		
		objectives.addValue(statistics.minObjective, "Minimum", "" + timepoint);
		objectives.addValue(statistics.avgObjective, "Average", "" + timepoint);
		objectives.addValue(statistics.maxObjective, "Maximum", "" + timepoint);
		
		memory.addValue(Memory.maxMemory(), "Maximum", "" + timepoint);
		memory.addValue(Memory.totalMemory(), "Total", "" + timepoint);
		memory.addValue(Memory.usedMemory(), "Used", "" + timepoint);
		
		time.addValue(statistics.branch, "Branch", "" + timepoint);
		time.addValue(statistics.norm, "Normalize", "" + timepoint);
		time.addValue(statistics.cluster, "Cluster", "" + timepoint);
		time.addValue(statistics.sort, "Sort", "" + timepoint);
		time.addValue(statistics.stats, "Stats", "" + timepoint);
	}

}
