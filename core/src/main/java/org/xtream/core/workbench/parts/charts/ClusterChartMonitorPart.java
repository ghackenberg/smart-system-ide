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

public class ClusterChartMonitorPart<T extends Component> extends ChartMonitorPart<T, CategoryTableXYDataset>
{
	
	public ClusterChartMonitorPart()
	{
		this(0, 0);
	}
	public ClusterChartMonitorPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ClusterChartMonitorPart(int x, int y, int width, int height)
	{
		super("Clusters", x, y, width, height);
		
		chart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
	}

	@Override
	protected CategoryTableXYDataset createDataset()
	{
		return new CategoryTableXYDataset();
	}
	@Override
	protected JFreeChart createChart()
	{
		return ChartFactory.createXYLineChart(null, null, "Count", dataset, PlotOrientation.VERTICAL, false, true, false);
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
		
		dataset.add(timepoint, equivalenceClasses.size(), "Aggregated");
	}

}
