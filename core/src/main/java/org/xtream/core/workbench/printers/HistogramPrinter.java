package org.xtream.core.workbench.printers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
import org.xtream.core.model.Component;
import org.xtream.core.model.Histogram;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ChartComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class HistogramPrinter<T extends Component> extends Part implements Printer<T>
{
	
	private static int PADDING = 25;
	private static int STROKE = 3;
	
	public HistogramPrinter()
	{
		this(0, 0);
	}
	public HistogramPrinter(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public HistogramPrinter(int x, int y, int width, int height)
	{
		super("Histogram printer", x, y, width, height);
	}

	@Override
	public void print(final T component, final int timepoint)
	{
		// Layouts
		
		final GridLayout chartLayout = new GridLayout();
		chartLayout.setHgap(1);
		chartLayout.setVgap(1);
		
		final GridLayout previewLayout = new GridLayout();
		previewLayout.setHgap(1);
		previewLayout.setVgap(1);
		
		// Panels
		
		final JPanel histograms = new JPanel(chartLayout);
		final JPanel previews = new JPanel(previewLayout);
		
		// Top pane
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(new JLabel("Component Histograms"), BorderLayout.PAGE_START);
		top.add(histograms, BorderLayout.CENTER);
		
		// Bottom pane
		
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(new JLabel("Child Component Previews"), BorderLayout.PAGE_START);
		bottom.add(new JScrollPane(previews), BorderLayout.CENTER);
		
		// Right pane
		
		final JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		
		// Tree view
		
		final JTree tree = new JTree(new ChartComponentTreeNode(null, component));
		
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override public void valueChanged(TreeSelectionEvent event)
				{
					// Remove old charts
					
					histograms.removeAll();
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
					
					if (componentNode.component.histograms.size() > 0)
					{	
						// Calculate grid layout
						
						int cols = (int) Math.ceil(Math.sqrt(componentNode.component.histograms.size()));
						int rows = (int) Math.ceil(Math.sqrt(componentNode.component.histograms.size()));
						
						chartLayout.setColumns(cols);
						chartLayout.setRows(rows);
						
						// Show charts
						
						for (Histogram definition : componentNode.component.histograms)
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
						
							JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
							
							chart.setAntiAlias(true);
							chart.setTextAntiAlias(true);
							chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
							
							for (int i = 0; i < definition.ports.length; i++)
							{
								chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
							}
							
							chart.getCategoryPlot().getDomainAxis().setVisible(false);
							
							ChartPanel panel = new ChartPanel(chart);
							
							histograms.add(panel);
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
							for (Histogram definition : child.histogramPreviews)
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
							
								JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
								
								chart.setAntiAlias(true);
								chart.setTextAntiAlias(true);
								chart.setPadding(new RectangleInsets(PADDING, PADDING, PADDING, PADDING));
								chart.removeLegend();
								
								for (int i = 0; i < definition.ports.length; i++)
								{
									chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(STROKE));
								}
								
								chart.getCategoryPlot().getDomainAxis().setVisible(false);
								
								ChartPanel histogrampanel = new ChartPanel(chart);
								
								histogrampanel.setPreferredSize(new Dimension(400, 300));
								
								previews.add(histogrampanel);
							}
						}
					}
					
					
					// Divider location
					
					right.setDividerLocation(right.getHeight() - 350);
					
					// Repaint frame
					
					histograms.updateUI();
					previews.updateUI();
				}
			}
		);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		// Left pane
		
		JPanel left = new JPanel(new BorderLayout());
		left.add(new JLabel("Component Hierarchy"), BorderLayout.PAGE_START);
		left.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		// Split pane

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		
		// Select row
		
		tree.setSelectionRow(0);
		
		// Divider location
		
		split.setDividerLocation(tree.getPreferredSize().width + 50);
		
		// Set component
		
		getPanel().add(split);
	}

}
