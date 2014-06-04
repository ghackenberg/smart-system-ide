package org.xtream.core.workbench.monitors;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.State;
import org.xtream.core.workbench.Monitor;

public class ChartMonitor extends Monitor
{
	
	private static int PADDING = 50;
	private static int STROKE = 3;
	
	private DefaultCategoryDataset states = new DefaultCategoryDataset();
	private DefaultCategoryDataset classes = new DefaultCategoryDataset();
	private DefaultCategoryDataset objectives = new DefaultCategoryDataset();
	private DefaultCategoryDataset memory = new DefaultCategoryDataset();
	private DefaultCategoryDataset time = new DefaultCategoryDataset();
	
	public ChartMonitor(JTabbedPane tabs)
	{
		super(tabs);
	}

	@Override
	public void start()
	{
		JFreeChart statesChart = ChartFactory.createLineChart("Number of generated, valid and dominant states", "Step", "Count", states, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart classesChart = ChartFactory.createLineChart("Number of equivalence classes", "Step", "Count", classes, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart objectivesChart = ChartFactory.createLineChart("Mininum, average and maximum objective", "Step", "Value", objectives, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart memoryChart = ChartFactory.createLineChart("Maximum, total and free memory", "Step", "Amount (in MB)", memory, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart timeChart = ChartFactory.createStackedAreaChart("Branch, norm, cluster, sort and stats time", "Step", "Amount (in ms)", time, PlotOrientation.VERTICAL, true, true, false);
		
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
		
		statesChart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
		classesChart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
		objectivesChart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
		memoryChart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
		timeChart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
		
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
		
		GridLayout layout = new GridLayout(2, 3);
		layout.setHgap(1);
		layout.setVgap(1);
		
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.add(statesPanel);
		panel.add(classesPanel);
		panel.add(objectivesPanel);
		panel.add(memoryPanel);
		panel.add(timePanel);
		
		tabs.addTab("Optimizer", panel);
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, long branch, long norm, long cluster, long sort, long stats, Map<Key, List<State>> equivalenceClasses)
	{
		states.addValue(generatedStates, "Generated states", "" + timepoint);
		states.addValue(validStates, "Valid states", "" + timepoint);
		states.addValue(dominantStates, "Dominant states", "" + timepoint);
		
		classes.addValue(equivalenceClasses.size(), "Equivalence classes", "" + timepoint);
		
		objectives.addValue(minObjective, "Min objective", "" + timepoint);
		objectives.addValue(avgObjective, "Avg objective", "" + timepoint);
		objectives.addValue(maxObjective, "Max objective", "" + timepoint);
		
		memory.addValue(maxMemory(), "Max memory", "" + timepoint);
		memory.addValue(totalMemory(), "Total memory", "" + timepoint);
		memory.addValue(usedMemory(), "Used memory", "" + timepoint);
		
		time.addValue(branch, "Branch time", "" + timepoint);
		time.addValue(norm, "Norm time", "" + timepoint);
		time.addValue(cluster, "Cluster time", "" + timepoint);
		time.addValue(sort, "Sort time", "" + timepoint);
		time.addValue(stats, "Stats time", "" + timepoint);
	}

	@Override
	public void stop()
	{
		
	}

}
