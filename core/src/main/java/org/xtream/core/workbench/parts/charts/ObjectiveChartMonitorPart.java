package org.xtream.core.workbench.parts.charts;

import java.awt.BasicStroke;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.workbench.parts.ChartMonitorPart;

public class ObjectiveChartMonitorPart<T extends Component> extends ChartMonitorPart<T, CategoryTableXYDataset>
{
	
	public ObjectiveChartMonitorPart()
	{
		this(0, 0);
	}
	public ObjectiveChartMonitorPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ObjectiveChartMonitorPart(int x, int y, int width, int height)
	{
		super("Objective", x, y, width, height);
		
		chart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
		chart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
		chart.getXYPlot().getRenderer().setSeriesStroke(2, new BasicStroke(STROKE));
	}

	@Override
	protected CategoryTableXYDataset createDataset()
	{
		return new CategoryTableXYDataset();
	}
	@Override
	protected JFreeChart createChart()
	{
		return ChartFactory.createXYLineChart(null, null, "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
	}
	
	@Override
	public void start()
	{
		super.start();
		
		dataset.clear();
	}
	
	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		super.handle(timepoint, statistics, equivalenceClasses, best);
		
		dataset.add(timepoint, statistics.minObjective, "Minimum");
		dataset.add(timepoint, statistics.avgObjective, "Average");
		dataset.add(timepoint, statistics.maxObjective, "Maximum");
	}

}
