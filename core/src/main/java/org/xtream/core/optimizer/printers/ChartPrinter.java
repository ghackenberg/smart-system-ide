package org.xtream.core.optimizer.printers;

import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;

public class ChartPrinter<T extends Component> extends Printer<T>
{

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
		
		ApplicationFrame frame = new ApplicationFrame("xtream - Printer");
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
						dataset.addValue(port.get(i), port.name, "" + i);
					}
				}
				
				JFreeChart chart = ChartFactory.createLineChart(contextComponent.name + "." + chartName, "Timepoint", "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
				
				ChartPanel panel = new ChartPanel(chart);
				
				frame.add(panel);
			}
		}
		
		// Show frame
		
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
