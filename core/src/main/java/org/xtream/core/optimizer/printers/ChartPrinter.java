package org.xtream.core.optimizer.printers;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;

public class ChartPrinter<T extends Component> extends Printer<T>
{
	
	private static int PADDING = 50;
	private static int STROKE = 1;
	
	private JTabbedPane tabs;
	
	public ChartPrinter(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

	@Override
	public void print(T component, int timepoint)
	{
		// Calculate number of charts
		
		int charts = 0;
		
		for (Entry<Component, Map<String, List<Port<Double>>>> mainEntry : component.chartsRecursive.entrySet())
		{
			charts += mainEntry.getValue().size();
		}
		
		// Calculate grid layout
		
		int cols = (int) Math.ceil(Math.sqrt(charts));
		int rows = (int) Math.ceil(Math.sqrt(charts));
		
		GridLayout layout = new GridLayout(cols, rows);
		layout.setHgap(1);
		layout.setVgap(1);
		
		// Initialize frame
		
		JPanel frame = new JPanel();
		frame.setLayout(layout);
		
		// Create charts
		
		for (Entry<Component, Map<String, List<Port<Double>>>> mainEntry : component.chartsRecursive.entrySet())
		{
			Component contextComponent = mainEntry.getKey();
			
			for (Entry<String, List<Port<Double>>> nestedEntry : mainEntry.getValue().entrySet())
			{
				String chartName = nestedEntry.getKey();
				
				List<Port<Double>> series = nestedEntry.getValue();
				
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				for (Port<Double> port : series)
				{
					for (int i = 0; i < timepoint; i++)
					{
						dataset.addValue(port.get(i), port.qualifiedName, "" + i);
					}
				}
				
				JFreeChart chart = ChartFactory.createLineChart(contextComponent.qualifiedName + "." + chartName, "Timepoint", null, dataset, PlotOrientation.VERTICAL, true, true, false);
				
				chart.setAntiAlias(true);
				chart.setTextAntiAlias(true);
				chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
				
				for (int i = 0; i < series.size(); i++)
				{
					chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
				}
				
				ChartPanel panel = new ChartPanel(chart);
				
				frame.add(panel);
			}
		}
		
		// Show frame
		
		tabs.addTab("Chart printer", frame);
	}

}
