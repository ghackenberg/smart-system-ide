package org.xtream.core.workbench.parts.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.workbench.parts.ChartMonitorPart;

public class TraceChartMonitorPart<T extends Component> extends ChartMonitorPart<T, DefaultXYDataset>
{
	
	public TraceChartMonitorPart()
	{
		this(0, 0);
	}
	public TraceChartMonitorPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public TraceChartMonitorPart(int x, int y, int width, int height)
	{
		super("Traces", x, y, width, height);
	}

	@Override
	protected DefaultXYDataset createDataset()
	{
		return new DefaultXYDataset();
	}
	@Override
	protected JFreeChart createChart()
	{
		return ChartFactory.createXYLineChart("Traces", null, "Objective", dataset, PlotOrientation.VERTICAL, false, true, false);
	}
	
	@Override
	public void start()
	{
		super.start();
		
		dataset = createDataset();
		
		chart.getXYPlot().setDataset(dataset);
	}
	
	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		super.handle(timepoint, statistics, equivalenceClasses, best);
		
		if (best != null)
		{
			dataset = createDataset();
			
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
				
				dataset.addSeries(series, data);
				
				if (Math.abs(data[1][timepoint] - statistics.maxObjective) < Math.abs(data[1][timepoint] - statistics.avgObjective))
				{
					chart.getXYPlot().getRenderer().setSeriesPaint(series, Color.GREEN);
				}
				else if (Math.abs(data[1][timepoint] - statistics.minObjective) < Math.abs(data[1][timepoint] - statistics.avgObjective))
				{
					chart.getXYPlot().getRenderer().setSeriesPaint(series, Color.RED);
				}
				else
				{
					chart.getXYPlot().getRenderer().setSeriesPaint(series, Color.BLUE);
				}
				
				chart.getXYPlot().getRenderer().setSeriesStroke(series, new BasicStroke(STROKE));
				
				series++;
			}
		}
		
		chart.getXYPlot().setDataset(dataset);
	}

}
