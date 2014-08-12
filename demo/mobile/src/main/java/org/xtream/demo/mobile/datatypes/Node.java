package org.xtream.demo.mobile.datatypes;

public class Node
{
	
	private String name;
	private Graph graph;
	
	private String weight;
	private String xpos;
	private String ypos;
	private String tag;
	
	public Node(String name, String weight, String xpos, String ypos, String tag)
	{
		this.name = name;
		this.weight = weight;
		this.xpos = xpos;
		this.ypos = ypos;
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
	
	public String getWeight()
	{
		return weight;
	}
	
	public String getXpos()
	{
		return xpos;
	}
	
	public String getYpos ()
	{
		return ypos;
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
