package org.xtream.core.workbench.parts;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.datatypes.Node;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.controls.ImagePanel;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.events.SelectionEvent;

public class AggregateNavigationGraphPart<T extends Component> extends Part<T>
{
	
	private ImagePanel image;
	
	private Graph graph;
	private Set<Float> colorValues;
    private HashMap<Edge, HashMap<Integer, Set<Edge>>> edgeMap = new HashMap<Edge, HashMap<Integer, Set<Edge>>>();
    private Set<Edge> printedEdges = new HashSet<Edge>();
    
	private Component root = null;
	private int timepoint = 0;
	
	public AggregateNavigationGraphPart(Graph graph)
	{
		this(graph, 0, 0);
	}
	public AggregateNavigationGraphPart(Graph graph, int x, int y)
	{
		this(graph, x, y, 1, 1);
	}
	public AggregateNavigationGraphPart(Graph graph, int x, int y, int width, int height)
	{
		super("Navigation graph", x, y, width, height);
		
		this.graph = graph;
		
		image = new ImagePanel();
		
		getPanel().add(image);
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
		
			root = selection.getElementByClass(Component.class);
			
			update();
		}
		else if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			timepoint = jump.getTimepoint();
			
			update();
		}
	}
	
	public void update() 
	{
		try
		{
			colorValues = new HashSet<Float>();
			
			PrintStream dot = new PrintStream(new File("AggrMobilityGraph.dot"));
			
			dot.append("digraph G {\n");
			dot.append("\tnormalize = 2.;\n");
			dot.append("\toverlap = false;\n");
			dot.append("\trankdir = LR;\n");
			dot.append("\tfontname = \"Calibri\";\n");
			dot.append("\tfontsize = 12;\n");
			dot.append("\tnode [fontname = \"Calibri\", fontsize = 10];\n");
			dot.append("\tedge [fontname = \"Calibri\", fontsize = 9];\n");
			
            if (graph != null)
            {
                dot.append("subgraph Map {\n");

                for (Edge e : graph.getEdges())
                {
                    Node source = graph.getNode(e.getSource());
                    Node target = graph.getNode(e.getTarget());

                    double length = graph.getEdgeDistance(source, target);

                    if (!source.equals(target))
                    {
                        dot.append("\t\"" + e.getSource() + "\" -> \"" + e.getTarget() + "\" [len = \"" + length + "\", color = grey, style = invis, arrowhead = vee, arrowsize = 0.5, penwidth = " + e.getWeight() + "];\n");
                    }
                }


                double nodeMaxWeight = 0.0;

                for (Node v : graph.getNodes())
                {
                    if (nodeMaxWeight < Double.parseDouble(v.getWeight()))
                    {
                        nodeMaxWeight = Double.parseDouble(v.getWeight());
                    }
                }

                for (Node v : graph.getNodes())
                {
                    int r, g, b;
                    r = g = b = (int) (127+(Double.parseDouble(v.getWeight())/nodeMaxWeight)*128);
                    String color = String.format("#%02x%02x%02x", r, g, b);

                    if (v.getName().contains("Origin"))
                    {
                        dot.append("\t\"" + v.getName() + "\" [label = \"\", width=.3, height=.2, shape = triangle, pendwidth = 0.1, color = black, fillcolor=\"" + color +"\"];\n");
                    }
                    else if (v.getName().contains("Destination"))
                    {
                        dot.append("\t\"" + v.getName() + "\" [label = \"\", width=.3, height=.2, shape = invtriangle, pendwidth = 0.1, color = black, style=filled, fillcolor=\"" + "black" +"\"];\n");
                    }
                    else
                    {
                        dot.append("\t\"" + v.getName() + "\" [label = \"\", shape = point, pendwidth = 0.25, color = black, fillcolor=\"" + color +"\"];\n");
                    }

                }

                dot.append("}\n");

                dot.append("subgraph Vehicle {\n");

                for (Component component : root.getChildrenByClass(Component.class)) 
                {
                    Set<String> set = new HashSet<String>();

                    Edge startPosition = null;
                    @SuppressWarnings("unused")
					Edge destinationPosition = null;
                    HashSet<Edge> edgeSet = null;
                    edgeSet = new HashSet<Edge>();


                    for (Port<?> port : component.getChildrenByClass(Port.class))
                    {

                        if (port.getName().equals("startPositionOutput"))
                        {
                            startPosition = (Edge) port.get(getState(), 0);
                        }

                        if (port.getName().equals("destinationPositionOutput"))
                        {
                            destinationPosition = (Edge) port.get(getState(), 0);
                        }

                        if (port.getName().equals("positionOutput") && !(set.contains(component.getName())))
                        {
                            set.add(component.getName());

                            for (int i = 0; i < timepoint; i++)
                            {
                                if (!edgeSet.contains((Edge) port.get(getState(), i)))
                                {
                                    edgeSet.add((Edge) port.get(getState(), i));
                                }
                            }
                        }

                    }

                    if (edgeMap.containsKey(startPosition))
                    {
                        HashMap<Integer, Set<Edge>> existingBigEdgeMap = edgeMap.get(startPosition);
                        existingBigEdgeMap.put(existingBigEdgeMap.keySet().size(), edgeSet);
                        edgeMap.put(startPosition, existingBigEdgeMap);
                    }
                    else {
                        HashMap<Integer, Set<Edge>> bigEdgeMap = new HashMap<Integer, Set<Edge>>();
                        bigEdgeMap.put(0, edgeSet);
                        edgeMap.put(startPosition, bigEdgeMap);
                    }

                }

                for (Edge startPosition : edgeMap.keySet())
                {
                    Color colorPartitioning = new Color(Color.HSBtoRGB(generateColor(0.10f), 1.f, 0.75f));
                    String color = String.format("#%02x%02x%02x", colorPartitioning.getRed(), colorPartitioning.getGreen(), colorPartitioning.getBlue());

                    HashMap<Integer, Set<Edge>> map = edgeMap.get(startPosition);
                    HashMap<Edge, Integer> edgeOccurence = new HashMap<Edge, Integer>();

                    for (int i = 0; i < map.size(); i++)
                    {
                        for (Edge e : map.get(i)) {

                            if (edgeOccurence.containsKey(e))
                            {
                                edgeOccurence.put(e, (edgeOccurence.get(e)+1));
                            }
                            else {
                                edgeOccurence.put(e, 1);
                            }
                        }
                    }

                    for (Edge e : edgeOccurence.keySet())
                    {
                        Double width = Double.parseDouble("" + edgeOccurence.get(e));
                        width /= 20.0;

                        if (!e.getSource().equals(e.getTarget())) {
                            dot.append("\t\"" + e.getSource() + "\" -> \"" + e.getTarget() + "\" [color = \"" + color + "\", arrowhead = vee, fontcolor = \"" + color + "\", penwidth = 1];\n");
                            printedEdges.add(e);
                        }
                    }

                }

                for (Edge e : graph.getEdges())
                {
                    Node source = graph.getNode(e.getSource());
                    Node target = graph.getNode(e.getTarget());

                    double length = graph.getEdgeDistance(source, target);

                    if (!source.equals(target) && (!printedEdges.contains(e)))
                    {
                        Double weight = (Double.parseDouble(e.getWeight())*0.25);
                        dot.append("\t\"" + e.getSource() + "\" -> \"" + e.getTarget() + "\" [len = \"" + length + "\", color = grey, style = dotted, arrowhead = vee, arrowsize = 0.5, penwidth = " + weight + "];\n");
                    }
                }

                dot.append("}\n");
			}
			
			dot.append("}");
			
			dot.close();
		
			Runtime.getRuntime().exec("dot -Kneato -Gdpi=250 -Gratio=1.0 -Tpng -oAggrMobilityGraph.png AggrMobilityGraph.dot").waitFor();
			Runtime.getRuntime().exec("dot -Kneato -Gdpi=250 -Gratio=1.0 -Tsvg -oAggrMobilityGraph.svg AggrMobilityGraph.dot").waitFor();
			
			image.setImage(ImageIO.read(new File("AggrMobilityGraph.png")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public float generateColor(float threshold) 
	{
		float color = (float) Math.random();
		
		for (float item : colorValues)
		{
			if (Math.abs(color-item) <= threshold)
			{
				return generateColor(threshold);
			}
		}
		
		colorValues.add(color);
		
		return color;
	}

}
