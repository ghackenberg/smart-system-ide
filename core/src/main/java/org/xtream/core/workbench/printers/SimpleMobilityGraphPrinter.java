package org.xtream.core.workbench.printers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.datatypes.Node;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ChartComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class SimpleMobilityGraphPrinter<T extends Component> extends Printer<T>
{
	
	private JTabbedPane tabs;
	private Graph graphRaw;
	
	@SuppressWarnings("rawtypes")
	Map<Port,Float> map = new HashMap<Port,Float>();
	
	public SimpleMobilityGraphPrinter(JTabbedPane tabs, Graph graph)
	{
		this.tabs = tabs;
		this.graphRaw = graph;
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
		
		// Slider 
		
		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, timepoint, timepoint);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		// Spinner
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(timepoint, 0, timepoint, 1));
		slider.setValue(timepoint);
		
		// Panel
		
		final JPanel previews = new JPanel();
		
		previews.setLayout(previewLayout);
		
		final JPanel graph = new JPanel();
		graph.setLayout(new BorderLayout());
		
		
		// Top pane
		
		JPanel top = new JPanel();
		
		top.setLayout(new BorderLayout());
		top.add(new JLabel("Mobility Graph"), BorderLayout.PAGE_START);
		top.add(new JScrollPane(graph), BorderLayout.CENTER);

		// Bottom pane
		
		JPanel bottom = new JPanel();
		
		bottom.setLayout(new BorderLayout());
		bottom.add(spinner, BorderLayout.WEST);
		bottom.add(slider, BorderLayout.CENTER);
		bottom.add(new JLabel("Steps"), BorderLayout.PAGE_START);
		
		bottom.setMinimumSize(new Dimension(0, 0));
		
		// Right pane
		
		final JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		right.setDividerLocation(slider.getPreferredSize().width*4);
		
		// Tree view
		
		final JTree tree = new JTree(new ChartComponentTreeNode(null, component));
		
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override 
				public void valueChanged(final TreeSelectionEvent event)
				{
					try
					{
						render(tree, slider.getValue(), graph);
					}
					catch (InterruptedException e)
					{
						throw new IllegalStateException(e);
					}
					catch (IOException e)
					{
						throw new IllegalStateException(e);
					}
				}
			}
		);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		slider.addChangeListener(new ChangeListener()
		{
			@Override 
			public void stateChanged(ChangeEvent changeEvent) 
			{
				spinner.setValue(slider.getValue());
				
				if (!slider.getValueIsAdjusting()) 
				{	
					try 
					{
						render(tree, slider.getValue(), graph);
					} 
					catch (InterruptedException e) 
					{
						throw new IllegalStateException(e);
					} 
					catch (IOException e) 
					{
						throw new IllegalStateException(e);
					}
				}
			}
		}
		);
		
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent changeEvent) 
			{
				slider.setValue((int) spinner.getValue());
				
				if (!slider.getValueIsAdjusting())
				{
					try 
					{
						render(tree, slider.getValue(), graph);
					} 
					catch (InterruptedException e) 
					{
						throw new IllegalStateException(e);
					} 
					catch (IOException e) 
					{
						throw new IllegalStateException(e);
					}
				}	
			}
		}
		);
		
		// Left pane
		
		JPanel left = new JPanel();
		
		left.setLayout(new BorderLayout());
		left.add(new JLabel("Component Hierarchy"), BorderLayout.PAGE_START);
		left.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		// Split pane

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		
		// Show frame
		
		tabs.addTab("Trace (SimpleMobilityGraph)", split);
		
		// Select row
		
		tree.setSelectionRow(0);
		
		// Divider location
		
		split.setDividerLocation(tree.getPreferredSize().width + 50);
	}
	
	public void render(JTree tree, int selectedTime, JPanel graph) throws InterruptedException, IOException 
	{	
		AbstractComponentTreeNode node = (AbstractComponentTreeNode) tree.getLastSelectedPathComponent();
		Component root = node.component;
		
		PrintStream dot = new PrintStream(new File("MobilityGraph.dot"));
		
		dot.append("digraph G {\n");
		dot.append("\toverlap = false;\n");
		dot.append("\tfontname = \"Calibri\";\n");
		dot.append("\tfontsize = 12;\n");
		dot.append("\tnode [fontname = \"Calibri\", fontsize = 10];\n");
		dot.append("\tedge [fontname = \"Calibri\", fontsize = 8];\n");
		
		if (graphRaw != null)
		{	
			dot.append("subgraph Map {\n");
			
			for (Edge e : graphRaw.getEdges())
			{
				double length = 1.0;
				dot.append("\t\"" + e.getSource() + "\" -> \"" + e.getTarget() + "\" [len = \"" + length + "\" ,color = grey, style = dashed, arrowhead = vee, penwidth = " + e.getWeight() + "];\n");
			}
			
			double nodeMaxWeight = 0.0;
			
			for (Node v : graphRaw.getNodes()) 
			{
				if (nodeMaxWeight < Double.parseDouble(v.getWeight())) 
				{
					nodeMaxWeight = Double.parseDouble(v.getWeight());
				}
			}
			
			for (Node v : graphRaw.getNodes()) 
			{
				int r, g, b;
				r = g = b = (int) (127+(Double.parseDouble(v.getWeight())/nodeMaxWeight)*128);
				String color = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
				
				dot.append("\t\"" + v.getName() + "\" [label = \"" + v.getName() + "\", style = filled, color = black, fillcolor=\"" + color +"\"];\n");
				// dot.append("\t\"" + v.getName() + "\" [label = \"\", shape = point, color=\"" + color +"\", pos=\""+ v.getXpos() + "," + v.getYpos() + "" + "\"];\n");
			}
			
			dot.append("}\n");
				
			dot.append("subgraph Vehicle {\n");

			// TODO [Dominik] Bug: not rendering when sequence is not yet complete

			for (Component component : root.components) 	
			{
			
			Set<String> set = new HashSet<String>();
			
			for (Port<?> port : component.ports)
			{
				
				//if (port.name.equals("positionOutput"))
				if (port.name.equals("positionOutput") && !(set.contains(component.name)))
				{	
					set.add(component.name);
					
					if (!map.containsKey(port))
					{
						map.put(port, (float) Math.random());
					}
					
					float saturation = 1.f;
					
					double width = 0.1;
					
					Edge previous = (Edge) port.get(0);
					
					int step = 0;
					
					for (int i = 0; i < selectedTime; i++)
					{	
						Edge current = (Edge) port.get(i);
						
						if (previous != current || ((i) == (selectedTime-1)))
						{
							float brightness = ((i-step) + 1.f + ((float) step-1.f) / 2.f)/(selectedTime);
							Color colorPartitioning = new Color(Color.HSBtoRGB(map.get(port), saturation, brightness));
							String color = "#" + Integer.toHexString(colorPartitioning.getRed()) + Integer.toHexString(colorPartitioning.getGreen()) + Integer.toHexString(colorPartitioning.getBlue());
							
							dot.append("\t\"" + previous.getSource() + "\" -> \"" + previous.getTarget() + "\" [label = \"" + "(" + (i-step) + "-" + (i-1) + ")" + "\", color = \"" + color + "\", fontcolor = \"" + color + "\", penwidth = " + width + "];\n");
							
							width = 0.1;
							step = 0;
						}
						
						previous = current;
						width+= 0.1;
						step++;
					}
				}
			}
		}
			dot.append("}\n");
		}
		
		dot.append("}");
		
		dot.close();
	
		Runtime.getRuntime().exec("dot -Ksfdp -Tpng -oMobilityGraph.png MobilityGraph.dot").waitFor();
		
		BufferedImage image = ImageIO.read(new File("MobilityGraph.png"));
		
		graph.removeAll();
		graph.add(new JLabel(new ImageIcon(image)));
		graph.updateUI();
		
	}

}
