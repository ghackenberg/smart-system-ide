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
import org.xtream.core.model.Component;
import org.xtream.core.model.Histogram;
import org.xtream.core.model.Port;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class HistogramPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private JPanel panel;
	private Component root = null;
	private int timepoint = 0;
	
	public HistogramPart()
	{
		this(0, 0);
	}
	public HistogramPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public HistogramPart(int x, int y, int width, int height)
	{
		super("Histogram printer", x, y, width, height);
		
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
					root = (Component) object;
					
					update();
					
					break;
				}
			}
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			timepoint = jump.timepoint;
			
			update();
		}
	}
	
	public void update()
	{
		// Remove old charts

		panel.removeAll();

		// Charts
		
		if (root.histograms.size() > 0)
		{	
			// Calculate grid layout
			
			int cols = (int) Math.ceil(Math.sqrt(root.histograms.size()));
			int rows = (int) Math.ceil(Math.sqrt(root.histograms.size()));
			
			panel.setLayout(new GridLayout(cols, rows));
			
			// Show charts
			
			for (Histogram definition : root.histograms)
			{
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				
				for (Port<?> port : definition.ports)
				{
					for (int i = 0; i < timepoint; i++)
					{
						if (!(map.containsKey(port.get(i).toString())))
						{
							map.put(port.get(i).toString(), 1);
						}
						else 
						{
							map.put(port.get(i).toString(), 1+map.get(port.get(i).toString()));
						}
					}
					
					for (String i : map.keySet())
					{
						dataset.setValue(map.get(i), i, "value");
					}				
					
				}
			
				JFreeChart chart = ChartFactory.createBarChart(definition.name, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
				
				chart.setAntiAlias(true);
				chart.setTextAntiAlias(true);
				chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
				
				for (int i = 0; i < definition.ports.length; i++)
				{
					chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
				}
				
				chart.getCategoryPlot().getDomainAxis().setVisible(false);
				
				ChartPanel panel = new ChartPanel(chart);
				
				this.panel.add(panel);
			}
		}
		
		// Repaint frame
		
		panel.updateUI();
	}

}
