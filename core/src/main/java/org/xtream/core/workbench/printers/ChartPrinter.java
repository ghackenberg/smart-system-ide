package org.xtream.core.workbench.printers;

import java.awt.BasicStroke;
import java.awt.GridLayout;

import javax.swing.JPanel;

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
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.SelectionEvent;

public class ChartPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Component root = null;
	private int timepoint = 0;
	
	public ChartPrinter()
	{
		this(0, 0);
	}
	public ChartPrinter(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ChartPrinter(int x, int y, int width, int height)
	{
		super("Chart printer", x, y, width, height);
		
		panel = new JPanel();
		
		getPanel().add(panel);
	}

	@Override
	public void print(final T component, final int timepoint)
	{
		this.timepoint = timepoint;
		
		update();
	}
	
	@Override
	public void handle(Event event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent selection = (SelectionEvent) event;
			
			for (Object object : selection.objects)
			{
				if (object instanceof Component)
				{
					root = (Component) object;
					
					update();
					
					break;
				}
			}
		}
	}
	
	public void update()
	{
		// Remove old charts
		
		panel.removeAll();

		// Charts
		
		if (root.charts.size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(root.charts.size()));
			int rows = (int) Math.ceil(Math.sqrt(root.charts.size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Chart definition : root.charts)
			{
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				for (Port<Double> port : definition.ports)
				{
					for (int i = 0; i < timepoint; i++)
					{
						dataset.addValue(port.get(i), port.name, "" + i);
					}
				}
				
				JFreeChart chart = ChartFactory.createLineChart(definition.name, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
				
				chart.setAntiAlias(true);
				chart.setTextAntiAlias(true);
				chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
				
				for (int i = 0; i < definition.ports.length; i++)
				{
					chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
				}
				
				chart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
				
				ChartPanel panel = new ChartPanel(chart);
				
				this.panel.add(panel);
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}

}
