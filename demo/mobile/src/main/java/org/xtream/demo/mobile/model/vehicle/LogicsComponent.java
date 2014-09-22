package org.xtream.demo.mobile.model.vehicle;

import java.util.LinkedList;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;

public class LogicsComponent extends Component
{
	
	public LogicsComponent(Graph graph, Double timeResolution, Double vMax) 
	{
		this.graph = graph;
		this.timeResolution = timeResolution;
		this.vMax = vMax;
	}
	
	// Parameters
	
	public Graph graph;
	public Double timeResolution;
	public Double vMax;
	
	// Inputs
	
	public Port<Edge> startPositionInput = new Port<>();
	public Port<Edge> destinationPositionInput = new Port<>();
	public Port<Double> positionTraversedLengthInput = new Port<>();
	public Port<Double> positionEdgeLengthInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Edge> positionOutput = new Port<>();
	public Port<LinkedList<Edge>> positionEdgeListOutput = new Port<>();
	public Port<List<Edge>> pathOutput = new Port<>();
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
				return positionEdgeListOutput.get(state, timepoint).getLast();
			}
		}
	};
	
	public Expression<LinkedList<Edge>> positionEdgeListExpression = new Expression<LinkedList<Edge>>(positionEdgeListOutput, true)	
	{
		@Override protected LinkedList<Edge> evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				LinkedList<Edge> pathEdges = new LinkedList<Edge>();
				pathEdges.add(startPositionInput.get(state, timepoint));
				return pathEdges;
			}
			else 
			{
				// Initialize with initial speed and subtract edge lengths
				double sum = (speedOutput.get(state, timepoint)/timeResolution);
				
				// Add already traversed length of edge in timepoint-1
				sum += positionTraversedLengthInput.get(state, timepoint-1);
				
				List<Edge> pathEdges = pathOutput.get(state, timepoint);
				
				// Create list of reached edges
				LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
				
				// Select every reachable edge and get weight
				for (int i = 0; i < pathEdges.size(); i++) 
				{
					Edge e = pathEdges.get(i);
					traversedEdges.add(e);
								
					if ((sum - Double.parseDouble(e.getWeight())) >= 0) 
					{
						sum -= Double.parseDouble(e.getWeight());
					}
					else 
					{
						// Traverse last edge partially and return result
						return traversedEdges;
					}
				}
			}
			LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
			traversedEdges.add(positionOutput.get(state, timepoint-1));
			return traversedEdges;	
		}
	};
	
	public Expression<List<Edge>> pathExpression = new Expression<List<Edge>>(pathOutput, true)	
	{
		@Override protected List<Edge> evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				List<Edge> pathEdges = new LinkedList<Edge>();
				pathEdges.add(startPositionInput.get(state, timepoint));
				return pathEdges;
			}
			else 
			{
				if (speedOutput.get(state, timepoint) > 0)
				{
					// Generate new random sequence of reachable edges
					return graph.generatePath(graph.getTargetNode(positionOutput.get(state, timepoint-1).getName()), graph.getTargetNode(destinationPositionInput.get(state, timepoint).getName()));	
				}
				else 
				{
					LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
					traversedEdges.add(positionOutput.get(state, timepoint-1));
					return traversedEdges;
				}
			}
		}
	};
	
	// Choose speed in km/h
	public Expression<Double> speedExpression = new Expression<Double>(speedOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorInput.get(state, timepoint))
			{
				if (!(positionOutput.get(state, timepoint-1).equals(destinationPositionInput.get(state, timepoint))))
				{
					return (vMax*Math.random());
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
	
	// Measures speed through crossed distance
	public Expression<Double> speedAbsoluteExpression = new Expression<Double>(speedAbsoluteOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorInput.get(state, timepoint))
			{
				if (!(positionOutput.get(state, timepoint-1).equals(destinationPositionInput.get(state, timepoint))))
				{
					return (speedOutput.get(state, timepoint)/timeResolution);
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
