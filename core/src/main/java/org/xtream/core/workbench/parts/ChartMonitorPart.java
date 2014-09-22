package org.xtream.core.workbench.parts;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Component;
import org.xtream.core.workbench.Part;

public abstract class ChartMonitorPart<T extends Component, S extends XYDataset> extends Part<T>
{

	protected static int PADDING = 0;
	protected static int STROKE = 3;
	
	protected JFreeChart chart;
	protected S dataset;
	
	public ChartMonitorPart(String name)
	{
		this(name, 0, 0);
	}
	public ChartMonitorPart(String name, int x, int y)
	{
		this(name, x, y, 1, 1);
	}
	public ChartMonitorPart(String name, int x, int y, int width, int height)
	{
		super(name, ChartMonitorPart.class.getClassLoader().getResource("parts/engine_monitor.png"), x, y, width, height);
		
		dataset = createDataset();
		
		chart = createChart();
		chart.setAntiAlias(true);
		chart.setTextAntiAlias(true);
		chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
		
		ChartPanel panel = new ChartPanel(chart);
		
		getPanel().add(panel);
	}
	
	protected abstract S createDataset();
	protected abstract JFreeChart createChart();
	
}
