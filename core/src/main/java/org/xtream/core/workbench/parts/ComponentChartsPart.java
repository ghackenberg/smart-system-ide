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
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class ComponentChartsPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Map<Chart, DefaultCategoryDataset> categoryDatasets = new HashMap<>();
	private Map<Chart, DefaultXYDataset> xyDatasets = new HashMap<>();
	private Component component;
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
		super("Component charts", x, y, width, height);
		
		panel = new JPanel();
		
		getPanel().add(panel);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			for (Object object : selection.getElements())
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
			
			update(jump.getTimepoint());
		}
	}
	
	public void update(Component component)
	{
		this.component = component;
		
		// Remove old charts
		
		panel.removeAll();

		// Charts
		
		if (component.getChildrenByClass(Timeline.class).size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(component.getChildrenByClass(Timeline.class).size()));
			int rows = (int) Math.ceil(Math.sqrt(component.getChildrenByClass(Timeline.class).size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Chart definition : component.getChildrenByClass(Chart.class))
			{
				JFreeChart jfreechart;
				
				if (definition instanceof Timeline)
				{
					DefaultXYDataset dataset = getXYDataset(definition);
					
					jfreechart = ChartFactory.createXYLineChart(definition.getName(), null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
					
					Timeline timeline = (Timeline) definition;
					
					for (int i = 0; i < timeline.getPorts().length; i++)
					{
						jfreechart.getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
					}
				}
				else if (definition instanceof Histogram)
				{
					DefaultCategoryDataset dataset = getCategoryDataset(definition);
					
					jfreechart = ChartFactory.createBarChart(definition.getName(), null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
					
					Histogram<?> histogram = (Histogram<?>) definition;
					
					for (int i = 0; i < histogram.getPorts().length; i++)
					{
						jfreechart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
					}
				}
				else
				{
					throw new IllegalStateException();
				}
				
				jfreechart.setAntiAlias(true);
				jfreechart.setTextAntiAlias(true);
				jfreechart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
				
				panel.add(new ChartPanel(jfreechart));
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
		
		for (Chart chart : component.getChildrenByClass(Chart.class))
		{
			//datasets.get(definition).clear();
			
			if (chart instanceof Timeline)
			{
				Timeline timeline = (Timeline) chart;
				
				for (Port<Double> port : timeline.getPorts())
				{
					double[][] data = new double[2][timepoint];
					
					for (int i = 0; i < timepoint; i++)
					{
						data[0][i] = i;
						data[1][i] = port.get(getState(), i);
					}
					
					xyDatasets.get(chart).addSeries(port.getName(), data);
				}
			}
			else if (chart instanceof Histogram)
			{
				Histogram<?> histogram = (Histogram<?>) chart;
				
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
						categoryDatasets.get(histogram).setValue(map.get(i), i, "value");
					}
				}
			}
			else
			{
				throw new IllegalStateException();
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}
	
	private DefaultCategoryDataset getCategoryDataset(Chart definition)
	{
		DefaultCategoryDataset dataset = categoryDatasets.get(definition);
		
		if (dataset == null)
		{
			dataset = new DefaultCategoryDataset();
			
			categoryDatasets.put(definition, dataset);
		}
		
		return dataset;
	}
	
	private DefaultXYDataset getXYDataset(Chart definition)
	{
		DefaultXYDataset dataset = xyDatasets.get(definition);
		
		if (dataset == null)
		{
			dataset = new DefaultXYDataset();
			
			xyDatasets.put(definition, dataset);
		}
		
		return dataset;
	}

}
