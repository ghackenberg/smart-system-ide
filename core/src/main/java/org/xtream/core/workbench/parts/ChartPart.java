package org.xtream.core.workbench.parts;

import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

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
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class ChartPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Map<Chart, DefaultCategoryDataset> datasets = new HashMap<>();
	private Component component;
	private int timepoint = 0;
	
	public ChartPart()
	{
		this(0, 0);
	}
	public ChartPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ChartPart(int x, int y, int width, int height)
	{
		super("Chart printer", x, y, width, height);
		
		panel = new JPanel();
		
		getPanel().add(panel);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			for (Object object : selection.objects)
			{
				if (object instanceof Component)
				{
					update((Component) object);
					
					break;
				}
			}
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			update(jump.timepoint);
		}
	}
	
	public void update(Component component)
	{
		this.component = component;
		
		// Remove old charts
		
		panel.removeAll();

		// Charts
		
		if (component.charts.size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(component.charts.size()));
			int rows = (int) Math.ceil(Math.sqrt(component.charts.size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Chart definition : component.charts)
			{
				DefaultCategoryDataset dataset = getDataset(definition);
				
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
			
			// Update datasets
			
			update(timepoint);
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	public void update(int timepoint)
	{
		this.timepoint = timepoint;
		
		for (Chart definition : component.charts)
		{
			//datasets.get(definition).clear();
			
			for (Port<Double> port : definition.ports)
			{
				for (int i = 0; i < timepoint; i++)
				{
					datasets.get(definition).addValue(port.get(i), port.name, "" + i);
				}
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	public DefaultCategoryDataset getDataset(Chart definition)
	{
		DefaultCategoryDataset dataset = datasets.get(definition);
		
		if (dataset == null)
		{
			dataset = new DefaultCategoryDataset();
			
			datasets.put(definition, dataset);
		}
		
		return dataset;
	}

}
