package org.xtream.demo.mobile.datatypes;

public class Edge 
{
	
	private String name;
	private String source;
	private String target;
	private String weight;
	private String tag;
	private Graph graph;
	
	public Edge(String name, String source, String target, String weight, String tag)
	{
		this.name = name;
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.tag = tag;
	}
	
	public Graph getGraph()
	{
		return graph;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getTarget()
	{
		return target;
	}
	
	public String getWeight()
	{
		return weight;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public String getIcon()
	{
		return "gfx/edge.png";
	}
	
	public void setGraph(Graph graph)
	{
		this.graph = graph;
	}
	
	public String toString()
	{
		return name;
	}

}
