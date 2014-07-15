package org.xtream.core.workbench.parts;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.xtream.core.model.Component;
import org.xtream.core.model.Container;
import org.xtream.core.model.Element;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.controls.ImagePanel;
import org.xtream.core.workbench.events.SelectionEvent;

public class ComponentArchitecturePart<T extends Component> extends Part<T>
{
	
	private ImagePanel image;
	
	public ComponentArchitecturePart()
	{
		this(0, 0);
	}
	public ComponentArchitecturePart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ComponentArchitecturePart(int x, int y, int width, int height)
	{
		super("Component architecture", ComponentArchitecturePart.class.getClassLoader().getResource("parts/component_architecture.png"), x, y, width, height);

		image = new ImagePanel();
		
		getPanel().add(image);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			Container root = selection.getElementByClass(Container.class);
			
			show(root);
		}
	}
	
	public void show(Container root)
	{
		try
		{
			Map<Element, Map<Element, List<String>>> edges = new HashMap<>();
			for (ChannelExpression<?> channel : root.getChildrenByClass(ChannelExpression.class))
			{
				Map<Element, List<String>> source = edges.get(channel.getSource().getParent());
				
				if (source == null)
				{
					source = new HashMap<>();
					edges.put(channel.getSource().getParent(), source);
				}
				
				List<String> target = source.get(channel.getPort().getParent());
				
				if (target == null)
				{
					target = new LinkedList<>();
					source.put(channel.getPort().getParent(), target);
				}
				
				target.add(channel.getName());
			}
			
			PrintStream dot = new PrintStream(new File("Graph.dot"));
			
			dot.append("digraph G {\n");
			dot.append("\tfontname = \"Calibri\";\n");
			dot.append("\tfontsize = 13;\n");
			dot.append("\tnode [fontname = \"Calibri\", fontsize = 11];\n");
			dot.append("\tedge [fontname = \"Calibri\", fontsize = 9];\n");
			for (Container container : root.getChildrenByClass(Container.class))
			{
				if (container.getChildrenByClass(Port.class).size() > 0)
				{
					dot.append("\t\"" + container.getName() + "_inputs\" [label = \"\", shape = point, color = white];\n");
				}
			}
			for (Container container : root.getChildrenByClass(Container.class))
			{
				if (container.getChildrenByClass(Port.class).size() > 0)
				{
					dot.append("\t\"" + container.getName() + "_outputs\" [label = \"\", shape = point, color = white];\n");
				}
			}
			dot.append("\tsubgraph \"cluster_" + root.getName() + "\" {\n");
			dot.append("\t\tlabel = \"" + root.getName() + " : " + root.getClass().getSimpleName() + "\";\n");
			dot.append("\t\tstyle = filled;\n");
			dot.append("\t\tfillcolor = gray95;\n");
			for (Container container : root.getChildrenByClass(Container.class))
			{
				if (container.getChildrenByClass(Port.class).size() > 0)
				{
					dot.append("\t\t\"" + container.getName() + "\" [label = \"" + container.getName() + "\", shape = rectangle, margin = " + (0.1 + container.getChildrenByClass(Component.class).size() / 10.) + " style = filled, fillcolor = gray85];\n");
				}
			}
			dot.append("\t}\n");
			for (Entry<Element, Map<Element, List<String>>> source : edges.entrySet())
			{
				for (Entry<Element, List<String>> target : source.getValue().entrySet())
				{
					String sourceName = source.getKey() == root ? target.getKey().getName() + "_inputs" : source.getKey().getName();
					String targetName = target.getKey() == root ? source.getKey().getName() + "_outputs" : target.getKey().getName();
					
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
