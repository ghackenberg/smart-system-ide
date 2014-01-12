package org.xtream.core.optimizer.printers;

import java.awt.BasicStroke;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;

public class ChartPrinter<T extends Component> extends Printer<T>
{
	
	private static int PADDING = 50;
	private static int STROKE = 3;
	
	private JTabbedPane tabs;
	
	public ChartPrinter(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

	@Override
	public void print(T component, int timepoint)
	{
		if (component.chartsRecursive.size() > 0)
		{
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(component.chartsRecursive.size()));
			int rows = (int) Math.ceil(Math.sqrt(component.chartsRecursive.size()));
			
			GridLayout layout = new GridLayout(cols, rows);
			layout.setHgap(1);
			layout.setVgap(1);
			
			// Initialize frame
			
			JPanel frame = new JPanel();
			frame.setLayout(layout);
			
			// Create charts
			
			for (Chart definition : component.chartsRecursive)
			{
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				for (Port<Double> port : definition.ports)
				{
					for (int i = 0; i < timepoint; i++)
					{
						dataset.addValue(port.get(i), port.qualifiedName, "" + i);
					}
				}
				
				JFreeChart chart = ChartFactory.createLineChart(definition.qualifiedName, "Time", null, dataset, PlotOrientation.VERTICAL, true, true, false);
				
				chart.setAntiAlias(true);
				chart.setTextAntiAlias(true);
				chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
				
				for (int i = 0; i < definition.ports.length; i++)
				{
					chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
				}
				
				chart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
				
				ChartPanel panel = new ChartPanel(chart);
				
				frame.add(panel);
			}
			
			// Show frame
			
			tabs.addTab("Chart printer", frame);
		}
	}

}
