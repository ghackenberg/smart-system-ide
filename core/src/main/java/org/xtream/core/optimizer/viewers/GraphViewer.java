package org.xtream.core.optimizer.viewers;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.optimizer.nodes.ComponentTreeNode;

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
		final JPanel graph = new JPanel();
		
		graph.setLayout(new GridLayout(1, 1));
		
		final JTree tree = new JTree(new ComponentTreeNode(null, root));

		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent event)
				{
					try
					{
						ComponentTreeNode node = (ComponentTreeNode) tree.getLastSelectedPathComponent();
						Component root = node.component;
						
						PrintStream dot = new PrintStream(new File("Graph.dot"));
						
						dot.append("digraph G {\n");
						dot.append("\tfontname = \"Calibri\";\n");
						dot.append("\tfontsize = 13;\n");
						dot.append("\tnode [fontname = \"Calibri\", fontsize = 11];\n");
						dot.append("\tedge [fontname = \"Calibri\", fontsize = 9];\n");
						for (Port<?> port : root.ports)
						{
							dot.append("\t\"" + port.name + "\" [label = \"\", shape = point, color = white];\n");
						}
						dot.append("\tsubgraph \"cluster_" + root.name + "\" {\n");
						dot.append("\t\tlabel = \"" + root.name + "\";\n");
						for (Component component : root.components)
						{
							dot.append("\t\t\"" + component.name + "\" [shape = rectangle];\n");
						}
						dot.append("\t}\n");
						for (ChannelExpression<?> channel : root.channels)
						{
							String source = channel.source.parent.name;
							String target = channel.port.parent.name;
							
							if (channel.source.parent == root)
							{
								source = channel.source.name;
							}
							if (channel.port.parent == root)
							{
								target = channel.port.name;
							}
							
							dot.append("\t\"" + source + "\" -> \"" + target + "\" [label = \"" + channel.name + "\", color = blue, fontcolor = blue];\n");
						}
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
		
		tree.setSelectionRow(0);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, new JScrollPane(graph));
		
		split.setDividerLocation(tree.getPreferredSize().width);
		
		tabs.addTab("Graph viewer", split);
	}

}
