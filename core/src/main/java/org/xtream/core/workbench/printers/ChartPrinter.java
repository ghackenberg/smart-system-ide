package org.xtream.core.workbench.printers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class ChartPrinter<T extends Component> extends Printer<T>
{
	
	private static int PADDING = 25;
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
		
		final GridLayout chartLayout = new GridLayout();
		
		chartLayout.setHgap(1);
		chartLayout.setVgap(1);
		
		// Create grid
		
		final GridLayout previewLayout = new GridLayout();
		
		previewLayout.setHgap(1);
		previewLayout.setVgap(1);
		
		// Panel
		
		final JPanel charts = new JPanel();
		
		charts.setLayout(chartLayout);
		
		// Panel
		
		final JPanel previews = new JPanel();
		
		previews.setLayout(previewLayout);
		
		// Top pane
		
		JPanel top = new JPanel();
		
		top.setLayout(new BorderLayout());
		top.add(new JLabel("Component Charts"), BorderLayout.PAGE_START);
		top.add(charts, BorderLayout.CENTER);
		
		// Bottom pane
		
		JPanel bottom = new JPanel();
		
		bottom.setLayout(new BorderLayout());
		bottom.add(new JLabel("Child Component Previews"), BorderLayout.PAGE_START);
		bottom.add(new JScrollPane(previews), BorderLayout.CENTER);
		
		// Right pane
		
		final JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		
		// Tree view
		
		final JTree tree = new JTree(new ComponentTreeNode(null, component));
		
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override public void valueChanged(TreeSelectionEvent event)
				{
					// Remove old charts
					
					charts.removeAll();
					previews.removeAll();
					
					// Obtain component
					
					AbstractComponentTreeNode componentNode = (AbstractComponentTreeNode) tree.getLastSelectedPathComponent();
					
					// Calculate previews
					
					int previewCount = 0;
					
					for (Component child : componentNode.component.components)
					{
						previewCount += child.previews.size();
					}

					// Charts
					
					if (componentNode.component.charts.size() > 0)
					{	
						// Calculate grid layout
						
						int cols = (int) Math.ceil(Math.sqrt(componentNode.component.charts.size()));
						int rows = (int) Math.ceil(Math.sqrt(componentNode.component.charts.size()));
						
						chartLayout.setColumns(cols);
						chartLayout.setRows(rows);
						
						// Show charts
						
						for (Chart definition : componentNode.component.charts)
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
							
							charts.add(panel);
						}
					}
					
					// Previews
					
					if (previewCount > 0)
					{
						// Calculate grid layout
						
						previewLayout.setColumns(previewCount);
						previewLayout.setRows(1);
						
						// Show charts
						
						for (Component child : componentNode.component.components)
						{
							for (Chart definition : child.previews)
							{
								DefaultCategoryDataset dataset = new DefaultCategoryDataset();
								
								for (Port<Double> port : definition.ports)
								{
									for (int i = 0; i < timepoint; i++)
									{
										dataset.addValue(port.get(i), port.name, "" + i);
									}
								}
								
								JFreeChart chart = ChartFactory.createLineChart(child.name + "." + definition.name, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
								
								chart.setAntiAlias(true);
								chart.setTextAntiAlias(true);
								chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
								chart.removeLegend();
								
								for (int i = 0; i < definition.ports.length; i++)
								{
									chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
								}
								
								chart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
								
								ChartPanel panel = new ChartPanel(chart);
								
								panel.setPreferredSize(new Dimension(400, 300));
								
								previews.add(panel);
							}
						}
					}
					
					// Divider location
					
					right.setDividerLocation(right.getHeight() - 350);
					
					// Repaint frame
					
					charts.updateUI();
					previews.updateUI();
				}
			}
		);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		// Left pane
		
		JPanel left = new JPanel();
		
		left.setLayout(new BorderLayout());
		left.add(new JLabel("Component Hierarchy"), BorderLayout.PAGE_START);
		left.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		// Split pane

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		
		// Show frame
		
		tabs.addTab("Trace (Chart)", split);
		
		// Select row
		
		tree.setSelectionRow(0);
		
		// Divider location
		
		split.setDividerLocation(tree.getPreferredSize().width + 50);
	}

}
