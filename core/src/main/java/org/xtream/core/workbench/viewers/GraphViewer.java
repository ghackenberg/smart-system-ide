package org.xtream.core.workbench.viewers;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JScrollPane;

import org.xtream.core.model.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.SelectionEvent;
import org.xtream.core.workbench.panels.ImagePanel;

public class GraphViewer<T extends Component> extends Part implements Viewer<T>
{
	
	private ImagePanel image;
	
	public GraphViewer()
	{
		this(0, 0);
	}
	public GraphViewer(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public GraphViewer(int x, int y, int width, int height)
	{
		super("Graph viewer", x, y, width, height);

		image = new ImagePanel();
		
		JScrollPane scroll = new JScrollPane(image);
		
		getPanel().add(scroll);
	}

	@Override
	public void view(T root)
	{
		
	}
	
	@Override
	public void handle(Event event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent selection = (SelectionEvent) event;
			
			for (Object object : selection.objects)
			{
				if (object instanceof Component)
				{
					Component root = (Component) object;
					
					show(root);
					
					break;
				}
			}
		}
	}
	
	public void show(Component root)
	{
		try
		{
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
			dot.append("\t\tlabel = \"" + root.name + " : " + root.getClass().getSimpleName() + "\";\n");
			dot.append("\t\tstyle = filled;\n");
			dot.append("\t\tfillcolor = gray95;\n");
			for (Component component : root.components)
			{
				if (component.ports.size() > 0)
				{
					dot.append("\t\t\"" + component.name + "\" [label = \"" + component.name + " : " + component.getClass().getSimpleName() + "\", shape = rectangle, margin = " + (0.1 + component.components.size() / 10.) + " style = filled, fillcolor = gray85];\n");
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
			
			image.setImage(ImageIO.read(new File("Graph.png")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
