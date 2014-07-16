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
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Container;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class ComponentChartsPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Map<Chart, JFreeChart> charts = new HashMap<>();
	private Container container;
	private int timepoint = 0;
	
	public ComponentChartsPart()
	{
		this(0, 0);
	}
	public ComponentChartsPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ComponentChartsPart(int x, int y, int width, int height)
	{
		super("Component charts", ComponentChartsPart.class.getClassLoader().getResource("parts/component_charts.png"), x, y, width, height);
		
		panel = new JPanel();
		
		getPanel().add(panel);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			Container container = selection.getElementByClass(Container.class);
			
			update(container);
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			int timepoint = jump.getTimepoint();
			
			update(timepoint);
		}
	}
	
	public void update(Container container)
	{
		this.container = container;
		
		// Remove old charts
		
		panel.removeAll();

		// Charts
		
		if (container.getChildrenByClass(Timeline.class).size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(container.getChildrenByClass(Timeline.class).size()));
			int rows = (int) Math.ceil(Math.sqrt(container.getChildrenByClass(Timeline.class).size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Chart definition : container.getChildrenByClass(Chart.class))
			{
				JFreeChart jfreechart = getChart(definition);
				
				panel.add(new ChartPanel(jfreechart));
				
				charts.put(definition, jfreechart);
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
		
		for (Chart definition : container.getChildrenByClass(Chart.class))
		{
			if (definition instanceof Timeline)
			{
				DefaultXYDataset dataset = new DefaultXYDataset();
				
				Timeline timeline = (Timeline) definition;
				
				for (Port<Double> port : timeline.getPorts())
				{
					double[][] data = new double[2][timepoint];
					
					for (int i = 0; i < timepoint; i++)
					{
						data[0][i] = i;
						data[1][i] = port.get(getState(), i);
					}
					
					dataset.addSeries(port.getQualifiedName(), data);
				}
				
				getChart(definition).getXYPlot().setDataset(dataset);
			}
			else if (definition instanceof Histogram)
			{
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				Histogram<?> histogram = (Histogram<?>) definition;
				
				for (Port<?> port : histogram.getPorts())
				{
					Map<String, Integer> map = new HashMap<String, Integer>();
					
					for (int i = 0; i < timepoint; i++)
					{
						if (!(map.containsKey(port.get(getState(), i).toString())))
						{
							map.put(port.get(getState(), i).toString(), 1);
						}
						else 
						{
							map.put(port.get(getState(), i).toString(), 1 + map.get(port.get(getState(), i).toString()));
						}
					}
					
					for (String i : map.keySet())
					{
						dataset.setValue(map.get(i), i, "value");
					}
				}
				
				getChart(definition).getCategoryPlot().setDataset(dataset);
			}
			else
			{
				throw new IllegalStateException();
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	private JFreeChart getChart(Chart definition)
	{
		JFreeChart jfreechart = charts.get(definition);
		
		if (jfreechart == null)
		{
			if (definition instanceof Timeline)
			{
				jfreechart = ChartFactory.createXYLineChart(definition.getName(), null, null, new DefaultXYDataset(), PlotOrientation.VERTICAL, true, true, false);
				
				Timeline timeline = (Timeline) definition;
				
				for (int series = 0; series < timeline.getPorts().length; series++)
				{
					jfreechart.getXYPlot().getRenderer().setSeriesStroke(series, new BasicStroke(STROKE));
				}
			}
			else if (definition instanceof Histogram)
			{
				jfreechart = ChartFactory.createBarChart(definition.getName(), null, null, new DefaultCategoryDataset(), PlotOrientation.VERTICAL, true, true, false);
			}
			else
			{
				throw new IllegalStateException();
			}
			
			jfreechart.setAntiAlias(true);
			jfreechart.setTextAntiAlias(true);
			jfreechart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
			
			charts.put(definition, jfreechart);
		}
			
		return jfreechart;
	}

}
