package org.xtream.core.workbench.parts;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.workbench.Part;

public class EquivalencesChartsPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	
	private List<Equivalence> equivalences;
	
	private GridLayout layout;
	private JPanel panel;
	private Map<String, JFreeChart> charts = new HashMap<>();
	private Map<String, ChartPanel> panels = new HashMap<>();
	private Map<String, Double> minimum_x = new HashMap<>();
	private Map<String, Double> minimum_y = new HashMap<>();
	private Map<String, Double> maximum_x = new HashMap<>();
	private Map<String, Double> maximum_y = new HashMap<>();
	
	public EquivalencesChartsPart()
	{
		this(0, 0);
	}
	public EquivalencesChartsPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public EquivalencesChartsPart(int x, int y, int width, int height)
	{
		super("Equivalences charts", EquivalencesChartsPart.class.getClassLoader().getResource("parts/component_charts.png"), x, y, width, height);
		
		layout = new GridLayout();
		layout.setHgap(PADDING);
		layout.setVgap(PADDING);
		
		panel = new JPanel();
		panel.setLayout(layout);
		
		getPanel().add(panel);
	}
	
	@Override
	public void setRoot(T root)
	{
		super.setRoot(root);
		
		equivalences = root.getDescendantsByClass(Equivalence.class);
		
		int count = equivalences.size() * (equivalences.size() - 1) / 2;
		
		int cols = (int) Math.ceil(Math.sqrt(count));
		int rows = (int) Math.ceil(Math.sqrt(count));
		
		layout.setColumns(cols);
		layout.setRows(rows);
		
		charts.clear();
		panels.clear();
		
		panel.removeAll();
		
		for (int i = 0; i < equivalences.size(); i++)
		{
			// Get outer
			Equivalence outer = equivalences.get(i);
			
			for (int j = i + 1; j < equivalences.size(); j++)
			{
				// Get inner
				Equivalence inner = equivalences.get(j);
				
				// Get name
				String long_name = outer.getQualifiedName() + " - " + inner.getQualifiedName();
				String short_name = outer.getName() + " - " + inner.getName();
				
				// Create chart
				JFreeChart chart = ChartFactory.createScatterPlot(short_name, outer.getName(), inner.getName(), new DefaultXYDataset(), PlotOrientation.VERTICAL, false, false, false);
				((NumberAxis) chart.getXYPlot().getDomainAxis()).setAutoRangeIncludesZero(false);
				((NumberAxis) chart.getXYPlot().getRangeAxis()).setAutoRangeIncludesZero(false);
				charts.put(long_name, chart);
				
				// Create panel
				ChartPanel chart_panel = new ChartPanel(chart);
				panels.put(long_name, chart_panel);
				
				// Create minimum
				minimum_x.put(long_name, Double.MAX_VALUE);
				minimum_y.put(long_name, Double.MAX_VALUE);
				
				// Create maximum
				maximum_x.put(long_name, -Double.MAX_VALUE);
				maximum_y.put(long_name, -Double.MAX_VALUE);
				
				// Add panel
				panel.add(chart_panel);
			}
		}
	}
	
	@Override
	public void start()
	{
		super.start();

		for (Entry<String, JFreeChart> entry : charts.entrySet())
		{
			entry.getValue().getXYPlot().setDataset(new DefaultXYDataset());
		}
		
		for (Entry<String, Double> entry : minimum_x.entrySet())
		{
			entry.setValue(Double.MAX_VALUE);
		}
		for (Entry<String, Double> entry : minimum_y.entrySet())
		{
			entry.setValue(Double.MAX_VALUE);
		}

		for (Entry<String, Double> entry : maximum_x.entrySet())
		{
			entry.setValue(-Double.MAX_VALUE);
		}
		for (Entry<String, Double> entry : maximum_y.entrySet())
		{
			entry.setValue(-Double.MAX_VALUE);
		}
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> clusters, State best)
	{
		super.handle(timepoint, statistics, clusters, best);
		
		if (clusters.size() > 0)
		{
			for (int i = 0; i < equivalences.size(); i++)
			{
				// Get outer
				Equivalence outer = equivalences.get(i);
				
				for (int j = i + 1; j < equivalences.size(); j++)
				{
					// Get inner
					Equivalence inner = equivalences.get(j);
					
					// Get name
					String long_name = outer.getQualifiedName() + " - " + inner.getQualifiedName();
					
					// Get chart
					JFreeChart chart = charts.get(long_name);
					
					// Create dataset
					DefaultXYDataset dataset = new DefaultXYDataset();
					
					int number = 0;
					
					double min_x = minimum_x.get(long_name);
					double min_y = minimum_y.get(long_name);
					
					double max_x = maximum_x.get(long_name);
					double max_y = maximum_y.get(long_name);
					
					for (Entry<Key, List<State>> cluster : clusters.entrySet())
					{
						double[][] data = new double[2][cluster.getValue().size()];
						
						// Add states
						int index = 0;
						
						for (State state : cluster.getValue())
						{
							data[0][index] = outer.getPort().get(state, timepoint);
							data[1][index] = inner.getPort().get(state, timepoint);
							
							min_x = Math.min(min_x, data[0][index]);
							min_y = Math.min(min_y, data[1][index]);
							
							max_x = Math.max(max_x, data[0][index]);
							max_y = Math.max(max_y, data[1][index]);
							
							index++;
						}
						
						dataset.addSeries(number++, data);
					}
					
					// Add range
					double[][] data = new double[2][4];
					
					data[0][0] = min_x;
					data[1][0] = min_y;
					
					data[0][1] = max_x;
					data[1][1] = max_y;
					
					data[0][2] = min_x;
					data[1][2] = max_y;
					
					data[0][3] = max_x;
					data[1][3] = min_y;
					
					dataset.addSeries(number++, data);
					
					// Set dataset					
					chart.getXYPlot().setDataset(dataset);
					
					// Set range		
					minimum_x.put(long_name, min_x);
					minimum_y.put(long_name, min_y);
					
					maximum_x.put(long_name, max_x);
					maximum_y.put(long_name, max_y);
				}
			}
		}
	}

}
