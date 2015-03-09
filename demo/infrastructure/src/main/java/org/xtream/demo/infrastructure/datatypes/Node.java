package org.xtream.demo.infrastructure.datatypes;

public class Node
{
	
	private String name;
	private Graph graph;
	
	private double xpos;
	private double ypos;
	private double zpos;
	private String tag;
	
	public Node(String name, String zpos, String xpos, String ypos, String tag)
	{
		this.name = name;
		this.xpos = Double.parseDouble(xpos);
		this.ypos = Double.parseDouble(ypos);
		this.zpos = Double.parseDouble(zpos);
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
	
	public Double getZPos()
	{
		return zpos;
	}
	
	public double getXPos()
	{
		return xpos;
	}
	
	public double getYPos ()
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
