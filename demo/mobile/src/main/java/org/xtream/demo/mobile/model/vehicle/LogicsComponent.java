package org.xtream.demo.mobile.model.vehicle;

import java.util.LinkedList;
import java.util.List;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;

public class LogicsComponent extends Component
{
	
	public LogicsComponent(Graph graph, Double vMax) 
	{
		this.graph = graph;
		this.vMax = vMax;
	}
	
	// Parameters
	
	public Graph graph;
	public Double vMax;
	
	// Inputs
	
	public Port<Edge> startPositionInput = new Port<>();
	public Port<Edge> destinationPositionInput = new Port<>();
	public Port<Edge> positionOutgoingEdgesInput = new Port<>();
	public Port<Double> positionTraversedLengthInput = new Port<>();
	public Port<Double> positionEdgeLengthInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Edge> positionOutput = new Port<>();
	public Port<LinkedList<Edge>> positionListOutput = new Port<>();
	public Port<Edge> positionTargetOutput = new Port<>();
	public Port<Double> speedOutput = new Port<>();
	public Port<Double> speedAbsoluteOutput = new Port<>();
	
	
	// Expressions
	
	public Expression<Edge> positionExpression = new Expression<Edge>(positionOutput, true)	
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return startPositionInput.get(state, timepoint);	
			}
			else 
			{
				return positionListOutput.get(state, timepoint).getLast();
			}
		}
	};
	
	public Expression<LinkedList<Edge>> positionListExpression = new Expression<LinkedList<Edge>>(positionListOutput, true)	
	{
		@Override protected LinkedList<Edge> evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
				traversedEdges.add(startPositionInput.get(state, timepoint));
				return traversedEdges;
			}
			else 
			{
				if (speedOutput.get(state, timepoint) > 0)
				{
					// Generate new random sequence of reachable edges
					List<Edge> edgeList = new LinkedList<Edge>();
					edgeList = graph.generatePath(graph.getTargetNode(positionOutput.get(state, timepoint-1).getName()), graph.getTargetNode(destinationPositionInput.get(state, timepoint).getName()));
					
					// Initialize with initial speed and substract edge lengths
					double sum = speedOutput.get(state,timepoint);
					
					// Add already traversed length of edge in timepoint-1
					sum += positionTraversedLengthInput.get(state, timepoint-1);
					
					// Create list of reached edges
					LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
					
					// Select every reachable edge and get weight
					for (int i = 0; i < edgeList.size(); i++) 
					{
						Edge e = edgeList.get(i);
						traversedEdges.add(e);
									
						if ((sum - Double.parseDouble(e.getWeight())) >= 0) 
						{
							sum -= Double.parseDouble(e.getWeight());
						}
						else {
							// Traverse last edge partially and return result
							return traversedEdges;
						}
					}
					
				}
				else 
				{
					LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
					traversedEdges.add(positionOutput.get(state, timepoint-1));
					return traversedEdges;
				}
			}
			LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
			traversedEdges.add(positionOutput.get(state, timepoint-1));
			return traversedEdges;
		}
	};
	

	public Expression<Edge> positionTargetExpression = new Expression<Edge>(positionTargetOutput, true)	
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			if (positionTraversedLengthInput.get(state, timepoint) >= positionEdgeLengthInput.get(state, timepoint))
			{
				List<Edge> edgeList = new LinkedList<Edge>();
				
				if (edgeList.isEmpty())
				{
					edgeList = graph.generatePath(graph.getTargetNode(positionOutput.get(state, timepoint).getName()), graph.getTargetNode(destinationPositionInput.get(state, timepoint).getName()));
					edgeList.add(destinationPositionInput.get(state, timepoint));
					
					return edgeList.get(0);
				}
				else 
				{
					edgeList = graph.generatePath(graph.getTargetNode(positionOutput.get(state, timepoint).getName()), graph.getTargetNode(destinationPositionInput.get(state, timepoint).getName()));
					
					return edgeList.get(0);
				}
			
			}
			else 
			{
				return positionOutput.get(state, timepoint);
			}
		}
	};
	
	// Crossed distance per step
	public Expression<Double> speedExpression = new Expression<Double>(speedOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorInput.get(state, timepoint))
			{
				if (!(positionOutput.get(state, timepoint-1).equals(destinationPositionInput.get(state, timepoint))))
				{
					return (Math.random()*vMax);
				}
				else 
				{
					return 0.;
				}
			}
			else 
			{
				return 0.;
			}
		}
	};
	
	// Measures speed in kilometers/h
	public Expression<Double> speedAbsoluteExpression = new Expression<Double>(speedAbsoluteOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorInput.get(state, timepoint))
			{
				if (!(positionTargetOutput.get(state, timepoint-1).equals(startPositionInput.get(state, timepoint))) && !(positionOutput.get(state, timepoint-1).equals(destinationPositionInput.get(state, timepoint))))
				{
					return (speedOutput.get(state, timepoint)*60);
				}
				else 
				{
					return 0.;
				}
			}
			else 
			{
				return 0.;
			}
		}
	};
	

}
