package org.xtream.demo.mobile.model.vehicles;

import java.util.LinkedList;
import java.util.List;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.AbstractLogicsComponent;

public class LogicsComponent extends AbstractLogicsComponent
{
	
	public LogicsComponent(Graph graph) 
	{
		this.graph = graph;
	}
	
	// Parameters
	
	private Graph graph;
	
	// Inputs
	
	public Port<Edge> startPositionInput = new Port<>();
	public Port<Edge> destinationPositionInput = new Port<>();
	public Port<Edge> positionOutgoingEdgesInput = new Port<>();
	public Port<Double> positionTraversedLengthInput = new Port<>();
	public Port<Double> positionEdgeLengthInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Edge> positionOutput = new Port<>();
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
				if (speedOutput.get(state, timepoint) > 0)
				{
					return positionTargetOutput.get(state, timepoint-1);
				}
				else 
				{
					return positionOutput.get(state, timepoint-1);
				}
			}
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
				if (!(positionTargetOutput.get(state, timepoint-1).equals(startPositionInput.get(state, timepoint))) && !(positionOutput.get(state, timepoint-1).equals(destinationPositionInput.get(state, timepoint))))
				{
					return (Math.random()*3.33);
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
