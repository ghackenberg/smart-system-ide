package org.xtream.core.optimizer.printers;

import java.awt.BasicStroke;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

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
import org.xtream.core.optimizer.printers.chart.ComponentNode;

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
	public void print(final T component, final int timepoint)
	{
		// Create grid
		
		final GridLayout layout = new GridLayout();
		
		layout.setHgap(1);
		layout.setVgap(1);
		
		// Initialize frame
		
		final JPanel frame = new JPanel();
		
		frame.setLayout(layout);
		
		// Tree view
		
		final JTree tree = new JTree(new ComponentNode(null, component));
		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override public void valueChanged(TreeSelectionEvent event)
				{
					// Remove old charts
					
					frame.removeAll();
					
					// Obtain component
					
					ComponentNode componentNode = (ComponentNode) tree.getLastSelectedPathComponent();
					
					System.out.println(componentNode.component.qualifiedName);

					// Check for charts
					
					if (componentNode.component.charts.size() > 0)
					{	
						// Calculate grid layout
						
						int cols = (int) Math.ceil(Math.sqrt(componentNode.component.charts.size()));
						int rows = (int) Math.ceil(Math.sqrt(componentNode.component.charts.size()));
						
						layout.setColumns(cols);
						layout.setRows(rows);
						
						// Show charts
						
						for (Chart definition : componentNode.component.charts)
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
					}
					
					// Repaint frame
					
					frame.updateUI();
				}
			}
		);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		tree.setSelectionRow(0);
		
		// Split pane

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, frame);
		split.setDividerLocation(200);
		
		// Show frame
		
		tabs.addTab("Chart printer", split);
	}

}
