package org.xtream.demo.infrastructure.datatypes;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.KShortestPaths;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xtream.demo.infrastructure.datatypes.helpers.Dom;


public class Graph
{
	//private WeightedGraph<Node, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	private AbstractBaseGraph<Node, DefaultWeightedEdge> graph = new DirectedWeightedMultigraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	private Map<String, Edge> edgeMap = new HashMap<String, Edge>();
	private Map<String, DefaultWeightedEdge> graphEdgeMap = new HashMap<String, DefaultWeightedEdge>();
	private Map<DefaultWeightedEdge, Edge> interEdgeMap = new HashMap<DefaultWeightedEdge, Edge>();
	private Map<Integer, Double> maxCostMap = new HashMap<Integer, Double>();
	private Map<Integer, List<GraphPath<Node, DefaultWeightedEdge>>> kShortestPathsMap = new HashMap<Integer, List<GraphPath<Node, DefaultWeightedEdge>>>();
	
	public Graph(String filename) 
	{
		try 
		{
			File file = new File(filename);
		
			String type = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			if (type.equals("xml"))
			{
				Document documentGraph = builder.parse(file);
				Element rootElement = documentGraph.getDocumentElement();
		
				for (Element child : Dom.getChildElementsByTagname(rootElement, "Node"))
				{
					this.addNode(new Node(child.getAttribute("name"), child.hasAttribute("weight") ? child.getAttribute("weight") : null, child.hasAttribute("xpos") ? child.getAttribute("xpos") : "0", child.hasAttribute("ypos") ? child.getAttribute("ypos") : "0", child.hasAttribute("tag") ? child.getAttribute("tag") : null));
				}
		
				for (Element child : Dom.getChildElementsByTagname(rootElement, "Edge"))
				{
					this.addEdge(new Edge(child.getAttribute("name"), child.getAttribute("source"), child.getAttribute("target"), child.hasAttribute("weight") ? child.getAttribute("weight") : null, child.hasAttribute("tag") ? child.getAttribute("tag") : null));
				}
			}
			else if (type.equals ("osm"))
			{
				Document documentGraph = builder.parse(file);
				Element rootElement = documentGraph.getDocumentElement();
				
				for (Element child : Dom.getChildElementsByTagname(rootElement, "way"))
				{
					for (Element tag : Dom.getChildElementsByTagname(child, "tag")) {
						
						if (tag.getAttribute("k").equals("highway"))
						{
							int listsize = Dom.getChildElementsByTagname(child, "nd").size();
							
							for (int i = 0; i < listsize-1; i++)
							{
								Element source = Dom.getChildElementsByTagname(child, "nd").get(i);
								Element target = Dom.getChildElementsByTagname(child, "nd").get(i+1);
								
								for (Element node : Dom.getChildElementsByTagname(rootElement, "node"))
								{
									if (source.getAttribute("ref").equals(node.getAttribute("id")))
									{
										this.addNode(new Node(node.getAttribute("id"), node.hasAttribute("version") ? node.getAttribute("version") : null, node.hasAttribute("lon") ? node.getAttribute("lon") : "0", node.hasAttribute("lat") ? node.getAttribute("lat") : "0", node.hasAttribute("tag") ? node.getAttribute("tag") : null));
									}
									
									if (target.getAttribute("ref").equals(node.getAttribute("id")))
									{
										this.addNode(new Node(node.getAttribute("id"), node.hasAttribute("version") ? node.getAttribute("version") : null, node.hasAttribute("lon") ? node.getAttribute("lon") : "0", node.hasAttribute("lat") ? node.getAttribute("lat") : "0", node.hasAttribute("tag") ? node.getAttribute("tag") : null));
									}
								}						
								
								this.addEdge(new Edge((source.getAttribute("ref")+target.getAttribute("ref")), source.getAttribute("ref"), target.getAttribute("ref"), child.hasAttribute("version") ? child.getAttribute("version") : null, child.hasAttribute("tag") ? child.getAttribute("tag") : null));
							}
						}
					}
				}
			}
			else if (type.equals ("graphml")) 
			{
				Document documentGraph = builder.parse(file);
				
				Element rootElement = documentGraph.getDocumentElement();
				
				Element graphElement = Dom.getChildElementByTagname(rootElement, "graph");
				
				// Assess lightest color for node weighting
				double lightestColor = 0;
				
				for (Element child : Dom.getChildElementsByTagname(graphElement, "node"))
				{
					for (Element child2 : Dom.getChildElementsByTagname(child, "data"))
						
						if (child2.hasChildNodes())
						{
							Element shapeNode = Dom.getChildElementByTagname(child2, "y:ShapeNode");
							
							Element fill = Dom.getChildElementByTagname(shapeNode, "y:Fill");
							
							if (lightestColor < (Color.decode(fill.getAttribute("color")).getRed()))
							{
								lightestColor = Color.decode(fill.getAttribute("color")).getRed();
							}
						}
				}
			
				for (Element child : Dom.getChildElementsByTagname(graphElement, "node"))
				{
					for (Element data : Dom.getChildElementsByTagname(child, "data"))
						
						if (data.hasChildNodes())
						{
							Element shapeNode = Dom.getChildElementByTagname(data, "y:ShapeNode");
							
							Element geometry = Dom.getChildElementByTagname(shapeNode, "y:Geometry");
							
							Element fill = Dom.getChildElementByTagname(shapeNode, "y:Fill");
							
							this.addNode(new Node(child.getAttribute("id"), fill.hasAttribute("color") ? String.valueOf((Color.decode(fill.getAttribute("color")).getRed()/lightestColor)) : null, geometry.hasAttribute("x") ? geometry.getAttribute("x") : "0", geometry.hasAttribute("y") ? geometry.getAttribute("y") : "0", child.hasAttribute("tag") ? child.getAttribute("tag") : null));
						}
				}
				
				for (Element child : Dom.getChildElementsByTagname(graphElement, "edge"))
				{
					for (Element data : Dom.getChildElementsByTagname(child, "data"))
						
						if (data.hasChildNodes())
						{
							Element polyLineEdge = Dom.getChildElementByTagname(data, "y:PolyLineEdge");
							
							Element lineStyle = Dom.getChildElementByTagname(polyLineEdge, "y:LineStyle");
							
							this.addEdge(new Edge(child.getAttribute("id"), child.getAttribute("source"), child.getAttribute("target"), lineStyle.hasAttribute("width") ? String.valueOf(0.5*Double.parseDouble(lineStyle.getAttribute("width"))) : null, child.hasAttribute("tag") ? child.getAttribute("tag") : null));
						}
				}	
			}
			else 
			{
				throw new IllegalStateException("Map file type not supported");
			}
		}
		catch (IOException | ParserConfigurationException | SAXException e) 
		{
			throw new IllegalStateException("Map file not found");
		}
	}

	public Set<Node> getNodes()
	{
		return graph.vertexSet();
	}
	
	public List<Edge> getEdges()
	{
		return new ArrayList<Edge>(edgeMap.values());
	}
	
	public Node addNode(Node node) 
	{
		node.setGraph(this);
		
		graph.addVertex(node);
		
		return node;
	}
	
	public Edge addEdge(Edge edge) 
	{	
		edge.setGraph(this);
		DefaultWeightedEdge graphEdge = new DefaultWeightedEdge();
		
		graph.addEdge(getNode(edge.getSource()), getNode(edge.getTarget()), graphEdge);
		graph.setEdgeWeight(graphEdge, edge.getWeight());
	
		graphEdgeMap.put(edge.getName(), graphEdge);
		edgeMap.put(edge.getName(), edge);
		interEdgeMap.put(graphEdge, edge);
		
		return edge;
	}
	
	public Node getNode(String name)
	{
		for (Node node : graph.vertexSet())
		{
			if (node.getName().equals(name))
			{
				return node;
			}
		}
		
		throw new IllegalStateException("Node not found: " + name);
	}
	
	public Node getSourceNode(String edge)
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			DefaultWeightedEdge graphEdge = graphEdgeMap.get(edge);
			
			return graph.getEdgeSource(graphEdge);
		}
		
		throw new IllegalStateException("Edge not found: " + edge);
	}

	public Node getTargetNode(String edge)
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			DefaultWeightedEdge graphEdge = graphEdgeMap.get(edge);
			
			return graph.getEdgeTarget(graphEdge);
		}
		
		throw new IllegalStateException("Edge not found: " + edge);
	}
	
	private Object getDefaultWeightedEdge(String edge)
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			return graphEdgeMap.get(edge);
		}
		
		throw new IllegalStateException("Edge not found: " + edge);
	}
	
	public Edge getEdge(String edge)
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			return edgeMap.get(edge);
		}
		
		throw new IllegalStateException("Edge not found: " + edge);
	}
	
	public Edge getConnectingEdge(Node source, Node target)
	{
		if (graph.containsEdge(source, target)) 
		{
			return interEdgeMap.get(graph.getEdge(source, target));
		}
		
		throw new IllegalStateException("Edge not found: (" + source + "," + target + ")");
	}
	
	public Set<Edge> getOutgoingEdges(Edge edge)
	{
		String edgename = edge.getName();
		
		DefaultWeightedEdge temp = (DefaultWeightedEdge) getDefaultWeightedEdge(edgename);
		
		Node target = graph.getEdgeTarget(temp);
		
		Set<Node> tempnodes = getOutgoingNodesAndSelf(target);
		
		Set<Edge> edges = new HashSet<Edge>();
		
		for (Node node : tempnodes) 
		{
			if (interEdgeMap.get(graph.getEdge(target, node)) != null) 
			{
				edges.add(interEdgeMap.get(graph.getEdge(target, node)));
			}
		}
		
		return edges;
	}
	
	public Set<Node> getOutgoingNodesAndSelf(Node node)
	{
		Set<Node> outgoingNodes = new HashSet<Node>();
		
		for (DefaultWeightedEdge graphEdge : graph.edgesOf(node))
		{
			if (graph.getEdgeTarget(graphEdge) != null )
			{
				outgoingNodes.add(graph.getEdgeTarget(graphEdge));
			}
		}
		
		if (!(outgoingNodes.isEmpty()))
		{
			return outgoingNodes;
		}
		
		throw new IllegalStateException("OutgoingNodes not found: " + node);
	}
	
	public List<Edge> getShortestPath(Node source, Node target)
	{
		List<Edge> edgeList = new LinkedList<Edge>();
		
		if (source.equals(target)) 
		{
			edgeList.add(getConnectingEdge(source, target));
			
			return edgeList;
		}
		
		BellmanFordShortestPath<Node, DefaultWeightedEdge> shortestPath = new BellmanFordShortestPath<Node, DefaultWeightedEdge> (graph, source);
		
		List<DefaultWeightedEdge> list = shortestPath.getPathEdgeList(target);
		
		if (list != null) 
		{
			for (DefaultWeightedEdge element : list) 
			{
				edgeList.add(interEdgeMap.get(element));
			}
			
			return edgeList;
		}
		
		throw new IllegalStateException("ShortestPath not found: (" + source + "," + target + ")");
	}
	
	public Boolean getExistShortestPath(Node source, Node target)
	{
		if (source.equals(target)) 
		{
			return true;
		}
		
		BellmanFordShortestPath<Node, DefaultWeightedEdge> shortestPath = new BellmanFordShortestPath<Node, DefaultWeightedEdge> (graph, source);
		
		List<DefaultWeightedEdge> list = shortestPath.getPathEdgeList(target);
		
		if (list != null) 
		{
			return true;
		}
		
		return false;
	}
	
	public List<Edge> getKShortestPath(Node source, Node target, int k)
	{
		System.out.println(source + "" + target + "" + k);
		List<Edge> edgeList = new LinkedList<Edge>();
		
		if (source.equals(target)) 
		{
			edgeList.add(getConnectingEdge(source, target));
			
			return edgeList;
		}
		
		KShortestPaths<Node, DefaultWeightedEdge> kShortestPaths = new KShortestPaths<Node, DefaultWeightedEdge>(graph, source, k);
		
		List<GraphPath<Node, DefaultWeightedEdge>> list = kShortestPaths.getPaths(target);
		
		if (list != null) 
		{
			GraphPath<Node, DefaultWeightedEdge> last = list.get(list.size()-1);
			
			for (DefaultWeightedEdge e : last.getEdgeList())
			{
				edgeList.add(interEdgeMap.get(e));
			}
			
			return edgeList;
		}
		
		throw new IllegalStateException("KShortestPath not found: (" + source + "," + target + ")");
		
	}
	
	public double getShortestPathCost(Node source, Node target)
	{
		if (source.equals(target)) 
		{
			return 0.;
		}
		
		BellmanFordShortestPath<Node, DefaultWeightedEdge> shortestPath = new BellmanFordShortestPath<Node, DefaultWeightedEdge> (graph, source);
		
		if ( !(shortestPath.equals(null)) ) 
		{
			return shortestPath.getCost(target);
		}
		
		throw new IllegalStateException("ShortestPathCost not found: (" + source + "," + target + ")");
	}
	
	public double getLongestPathCost(Node source, Node target)
	{
		if (source.equals(target)) 
		{
			return 0;
		}
		
		int hashValue = Objects.hash(source, target);
		
		if (maxCostMap.containsKey(hashValue))
		{
			return maxCostMap.get(hashValue);
		}
		
		KShortestPaths<Node, DefaultWeightedEdge> kShortestPaths = new KShortestPaths<Node, DefaultWeightedEdge>(graph, source, Integer.MAX_VALUE);
		
		List<GraphPath<Node, DefaultWeightedEdge>> list = kShortestPaths.getPaths(target);
		
		if (list != null) 
		{
			
			GraphPath<Node, DefaultWeightedEdge> last = list.get(list.size()-1);
			
			double maxCost = last.getWeight();
			
			maxCostMap.put(hashValue, maxCost);
			
			return maxCost;
		}
		
		throw new IllegalStateException("MaximumPathCost not found: (" + source + "," + target + ")");
	}
	
	public double getEdgeDistance(Node source, Node target) 
	{
		if (target.getZPos() == null || target.getZPos() == 0. || target.getZPos() == null || target.getZPos() == 0.) 
		{
			double differenceX = Math.pow(target.getXPos()-source.getXPos(), 2);
			double differenceY = Math.pow(target.getYPos()-source.getYPos(), 2);
			
			return Math.sqrt(differenceX+differenceY);
		}
		else 
		{
			double differenceX = Math.pow(target.getXPos()-source.getXPos(), 2);
			double differenceY = Math.pow(target.getYPos()-source.getYPos(), 2);
			double differenceZ = Math.pow(target.getZPos()-source.getZPos(), 2);
			
			return Math.sqrt(differenceX+differenceY+differenceZ);
		}
	}	
	
	public double getEdgeCapacity(Node source, Node target) 
	{
		return graph.getAllEdges(source, target).size();
		
	}
	
	public double getEdgeCapacity(String edge) 
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			return graph.getAllEdges(getSourceNode(edge), getTargetNode(edge)).size();
		}
		
		throw new IllegalStateException("Edge not found: " + edge ); 
		
	}
	
	public double getEdgeWeight(String edge) 
	{
		if (graphEdgeMap.containsKey(edge)) 
		{
			return (Double) graph.getEdgeWeight(graphEdgeMap.get(edge));
		}
		
		throw new IllegalStateException("Edge not found: " + edge ); 
	}
	
	@Override
	public String toString()
	{
		return graph.toString();
	}

	public List<Edge> generatePath(Node source, Node target) {
	
		List<Edge> edgeList = new LinkedList<Edge>();
		int alternatives = 2;
		
		if (source.equals(target)) 
		{
			edgeList.add(getConnectingEdge(source, target));
			
			return edgeList;
		}
		
		int hashValue = Objects.hash(source, target, alternatives);
		List<GraphPath<Node, DefaultWeightedEdge>> list;
		
		if (!kShortestPathsMap.containsKey(hashValue)) 
		{
			KShortestPaths<Node, DefaultWeightedEdge> kShortestPaths = new KShortestPaths<Node, DefaultWeightedEdge>(graph, source, alternatives);
			list = kShortestPaths.getPaths(target);
			kShortestPathsMap.put(hashValue, list);
		}
		
		else {
			list = kShortestPathsMap.get(hashValue);
		}
		
		if (list != null) 
		{
			GraphPath<Node, DefaultWeightedEdge> last = list.get((int) (list.size()*(Math.random()*0.51)));
			//GraphPath<Node, DefaultWeightedEdge> last = list.get((int) (list.size()*(Math.random())));
			
			for (DefaultWeightedEdge e : last.getEdgeList())
			{
				edgeList.add(interEdgeMap.get(e));
			}
			return edgeList;
		}
		
		throw new IllegalStateException("KShortestPath not found: (" + source + "," + target + ")");
	}
}
	
