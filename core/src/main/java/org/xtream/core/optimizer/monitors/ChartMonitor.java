package org.xtream.core.optimizer.monitors;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class ChartMonitor extends Monitor
{
	
	private static int PADDING = 50;
	private static int STROKE = 2;
	
	private DefaultCategoryDataset states = new DefaultCategoryDataset();
	private DefaultCategoryDataset classes = new DefaultCategoryDataset();
	private DefaultCategoryDataset objectives = new DefaultCategoryDataset();
	private DefaultCategoryDataset memory = new DefaultCategoryDataset();

	@Override
	public void start()
	{
		JFreeChart statesChart = ChartFactory.createLineChart("Number of generated, valid and dominant states", "Timepoint", "Count", states, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart classesChart = ChartFactory.createLineChart("Number of equivalence classes", "Timepoint", "Count", classes, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart objectivesChart = ChartFactory.createLineChart("Mininum, average and maximum objective", "Timepoint", "Value", objectives, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart memoryChart = ChartFactory.createLineChart("Maximum, total and free memory", "Timepoint", "Amount (in MB)", memory, PlotOrientation.VERTICAL, true, true, false);
		
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
		
		statesChart.setTextAntiAlias(true);
		classesChart.setTextAntiAlias(true);
		objectivesChart.setTextAntiAlias(true);
		memoryChart.setTextAntiAlias(true);
		
		statesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		classesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		objectivesChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		memoryChart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		
		ChartPanel statesPanel = new ChartPanel(statesChart);
		ChartPanel classesPanel = new ChartPanel(classesChart);
		ChartPanel objectivesPanel = new ChartPanel(objectivesChart);
		ChartPanel memoryPanel = new ChartPanel(memoryChart);
		
		GridLayout layout = new GridLayout(2, 2);
		layout.setHgap(1);
		layout.setVgap(1);
		
		ApplicationFrame frame = new ApplicationFrame("xtream - Engine Monitor");
		frame.setLayout(layout);
		frame.add(statesPanel);
		frame.add(classesPanel);
		frame.add(objectivesPanel);
		frame.add(memoryPanel);
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses)
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
		memory.addValue(freeMemory(), "Free memory", "" + timepoint);
	}

	@Override
	public void stop()
	{
		
	}

}
