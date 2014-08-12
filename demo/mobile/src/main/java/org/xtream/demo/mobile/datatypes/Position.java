package org.xtream.demo.mobile.datatypes;

public class Position {

	private Edge positionOnGraph;
	private Double positionOnEdge;
	
	public Position(Edge positionOnGraph, Double positionOnEdge) 
	{
		this.positionOnGraph = positionOnGraph;
		this.positionOnEdge = positionOnEdge;
	}
	
	public void setPositionOnGraph(Edge positionOnGraph) 
	{
		this.positionOnGraph = positionOnGraph;
	}
	
	public void setPositionOnGraph(Double positionOnEdge) 
	{
		this.positionOnEdge = positionOnEdge;
	}
	
	public Edge getPositionOnGraph() 
	{
		return positionOnGraph;
	}
	
	public Double getPositionOnEdge() 
	{
		return positionOnEdge;
	}
	
	public String toString() {
		return ("(" + positionOnGraph + "/" + positionOnEdge + ")");
	}
}
