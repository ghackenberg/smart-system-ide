package org.xtream.core.workbench.parts;

import java.awt.BasicStroke;
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
import org.jfree.data.xy.CategoryTableXYDataset;
import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.optimizer.beam.Key;
import org.xtream.core.workbench.Part;

public class EquivalenceChartsPart<T extends Component> extends Part<T>
{
	
	private static int PADDING = 0;
	private static int STROKE = 3;
	
	private List<Equivalence> equivalences;
	
	private GridLayout layout;
	private JPanel panel;
	private Map<Equivalence, CategoryTableXYDataset> datasets = new HashMap<>();
	private Map<Equivalence, JFreeChart> charts = new HashMap<>();
	private Map<Equivalence, ChartPanel> panels = new HashMap<>();
	
	public EquivalenceChartsPart()
	{
		this(0, 0);
	}
	public EquivalenceChartsPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public EquivalenceChartsPart(int x, int y, int width, int height)
	{
		super("Component charts", EquivalenceChartsPart.class.getClassLoader().getResource("parts/component_charts.png"), x, y, width, height);
		
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
		
		int cols = (int) Math.ceil(Math.sqrt(equivalences.size()));
		int rows = (int) Math.ceil(Math.sqrt(equivalences.size()));
		
		layout.setColumns(cols);
		layout.setRows(rows);
		
		charts.clear();
		datasets.clear();
		panels.clear();
		
		panel.removeAll();
		
		for (Equivalence equivalence : equivalences)
		{
			CategoryTableXYDataset dataset = new CategoryTableXYDataset();
			datasets.put(equivalence, dataset);
			
			JFreeChart chart = ChartFactory.createXYLineChart(equivalence.getQualifiedName(), "Step", "Value", dataset, PlotOrientation.VERTICAL, true, false, false);
			chart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(STROKE));
			chart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(STROKE));
			((NumberAxis) chart.getXYPlot().getRangeAxis()).setAutoRangeIncludesZero(false);
			charts.put(equivalence, chart);
			
			ChartPanel chart_panel = new ChartPanel(chart);
			panels.put(equivalence, chart_panel);
			
			panel.add(chart_panel);
		}
	}
	
	@Override
	public void start()
	{
		super.start();
		
		for (Entry<Equivalence, CategoryTableXYDataset> entry : datasets.entrySet())
		{
			entry.getValue().clear();
		}
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> clusters, State best)
	{
		super.handle(timepoint, statistics, clusters, best);
		
		if (clusters.size() > 0 && statistics.minEquivalences != null && statistics.maxEquivalences != null)
		{
			for (int i = 0; i < equivalences.size(); i++)
			{
				datasets.get(equivalences.get(i)).add(timepoint, statistics.minEquivalences[i], "Minimum");
				datasets.get(equivalences.get(i)).add(timepoint, statistics.maxEquivalences[i], "Maximum");
			}
		}
	}

}
