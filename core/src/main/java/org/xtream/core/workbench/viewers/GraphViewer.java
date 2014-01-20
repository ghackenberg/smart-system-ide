package org.xtream.core.workbench.viewers;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.xtream.core.model.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ArchitectureComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class GraphViewer<T extends Component> extends Viewer<T>
{
	
	private JTabbedPane tabs;
	
	public GraphViewer(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

	@Override
	public void view(T root)
	{
		// Graph pane
		
		final JPanel graph = new JPanel();
		
		graph.setLayout(new GridLayout(1, 1));
		
		// Tree pane
		
		final JTree tree = new JTree(new ArchitectureComponentTreeNode(null, root));

		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent event)
				{
					try
					{
						AbstractComponentTreeNode node = (AbstractComponentTreeNode) tree.getLastSelectedPathComponent();
						Component root = node.component;
						
						Map<Component, Map<Component, List<String>>> edges = new HashMap<>();
						for (ChannelExpression<?> channel : root.channels)
						{
							Map<Component, List<String>> source = edges.get(channel.source.parent);
							
							if (source == null)
							{
								source = new HashMap<>();
								edges.put(channel.source.parent, source);
							}
							
							List<String> target = source.get(channel.port.parent);
							
							if (target == null)
							{
								target = new LinkedList<>();
								source.put(channel.port.parent, target);
							}
							
							target.add(channel.name);
						}
						
						PrintStream dot = new PrintStream(new File("Graph.dot"));
						
						dot.append("digraph G {\n");
						dot.append("\tfontname = \"Calibri\";\n");
						dot.append("\tfontsize = 13;\n");
						dot.append("\tnode [fontname = \"Calibri\", fontsize = 11];\n");
						dot.append("\tedge [fontname = \"Calibri\", fontsize = 9];\n");
						for (Component component : root.components)
						{
							if (component.ports.size() > 0)
							{
								dot.append("\t\"" + component.name + "_inputs\" [label = \"\", shape = point, color = white];\n");
							}
						}
						for (Component component : root.components)
						{
							if (component.ports.size() > 0)
							{
								dot.append("\t\"" + component.name + "_outputs\" [label = \"\", shape = point, color = white];\n");
							}
						}
						dot.append("\tsubgraph \"cluster_" + root.name + "\" {\n");
						dot.append("\t\tlabel = \"" + root.name + " : " + root.getClass().getName() + "\";\n");
						dot.append("\t\tstyle = filled;\n");
						dot.append("\t\tfillcolor = gray95;\n");
						for (Component component : root.components)
						{
							if (component.ports.size() > 0)
							{
								dot.append("\t\t\"" + component.name + "\" [label = \"" + /*component.name + " : " +*/ component.getClass().getSimpleName() + "\", shape = rectangle, margin = " + (0.1 + component.components.size() / 10.) + " style = filled, fillcolor = gray85];\n");
							}
						}
						dot.append("\t}\n");
						for (Entry<Component, Map<Component, List<String>>> source : edges.entrySet())
						{
							for (Entry<Component, List<String>> target : source.getValue().entrySet())
							{
								String sourceName = source.getKey() == root ? target.getKey().name + "_inputs" : source.getKey().name;
								String targetName = target.getKey() == root ? source.getKey().name + "_outputs" : target.getKey().name;
								
								String label = "";
								
								for (String channel : target.getValue())
								{
									label += (label.length() > 0 ? "\n" : "") + channel;
								}
								
								dot.append("\t\"" + sourceName + "\" -> \"" + targetName + "\" [label = \"" + label + "\", color = blue, fontcolor = blue, penwidth = " + target.getValue().size() + "];\n");
							}
						}
						/*
						for (Component componentA : root.components)
						{
							if (componentA.ports.size() > 0)
							{
								for (Component componentB : root.components)
								{
									if (componentB.ports.size() > 0)
									{
										dot.append("\t\t\"" + componentA.name + "_inputs\" -> \"" + componentB.name + "\" -> \"" + componentA.name + "_outputs\" [style = invis];\n");
									}
								}
							}
						}
						*/
						dot.append("}");
						
						dot.close();
					
						Runtime.getRuntime().exec("dot -Tpng -oGraph.png Graph.dot").waitFor();
						
						BufferedImage image = ImageIO.read(new File("Graph.png"));
						
						graph.removeAll();
						graph.add(new JLabel(new ImageIcon(image)));
						graph.updateUI();
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
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		// Left pane
		
		JPanel left = new JPanel();
		
		left.setLayout(new BorderLayout());
		left.add(new JLabel("Component Hierarchy"), BorderLayout.PAGE_START);
		left.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		// Right pane
		
		JPanel right = new JPanel();
		
		right.setLayout(new BorderLayout());
		right.add(new JLabel("Component Graph"), BorderLayout.PAGE_START);
		right.add(new JScrollPane(graph), BorderLayout.CENTER);
		
		// Split pane
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		
		// Add tab
		
		tabs.addTab("Architecture", split);
		
		// Select row
		
		tree.setSelectionRow(0);

		// Divider location
		
		split.setDividerLocation(tree.getPreferredSize().width + 50);
	}

}
