package org.xtream.demo.infrastructure.datatypes;


public class Edge 
{
	
	private String name;
	private String source;
	private String target;
	private double weight;
	private String tag;
	private Graph graph;
	
	public Edge(String name, String source, String target, String weight, String tag)
	{
		this.name = name;
		this.source = source;
		this.target = target;
		this.weight = Double.parseDouble(weight);
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
	
	public Double getWeight()
	{
		return weight;
	}
	
	public String getTag()
	{
		return tag;
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
