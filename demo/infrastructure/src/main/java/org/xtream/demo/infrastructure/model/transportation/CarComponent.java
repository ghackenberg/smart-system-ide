package org.xtream.demo.infrastructure.model.transportation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.datatypes.Node;
import org.xtream.demo.infrastructure.model.EnergyComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;

public class CarComponent extends EnergyComponent
{

    // Parameters
	
	public Graph context;
	
    public Edge startPosition; 
    public Edge destinationPosition; 
    public HashSet<Edge> chargingStationPositions;
    
	public Double stateOfChargeWeight;
    public Double timeWeight; // relative
    public Double powerWeight; // relative
    public Double priority;
    
    public Double vehicleLength; // in km
    public Double vehicleWeight; // in t
    
    public Double enginePerformance; // in kwH
    public Double engineEfficiency; // Constant in kwH conversion to powertrain = velocity
    
    public Double stateOfCharge; // in kwH
    public Double stateOfChargeRateInflow; // (maximum) in kwH, for outputrate no limit is assumed
    public Double stateOfChargeRateOutflow; // in kwH
    public Double stateOfChargeMinimum; // in kwH
    public Double stateOfChargeMaximum; // in kwH
    
    public Double vehicleRangeAnxiety; // relative
    public Double chargingStationSelectionRandomness; // relative
    
    // Previews

	public CarComponent(Graph context, Edge startPosition, Edge destinationPosition, HashSet<Edge> chargingStationPositions, Double stateOfChargeWeight, Double timeWeight, Double powerWeight, Double priority, Double vehicleLength, Double vehicleWeight, Double enginePerformance, Double engineEfficiency, Double stateOfCharge, Double stateOfChargeRateInflow, Double stateOfChargeRateOutflow, Double stateOfChargeMinimum, Double stateOfChargeMaximum, Double vehicleRangeAnxiety, Double chargingStationSelectionRandomness)
	{
		this.context = context;
		this.startPosition = startPosition;
		this.destinationPosition = destinationPosition;
		this.chargingStationPositions = chargingStationPositions;
		this.stateOfChargeWeight = stateOfChargeWeight;
		this.timeWeight = timeWeight;
		this.powerWeight = powerWeight;
		this.priority = priority;
		this.vehicleLength = vehicleLength;
		this.vehicleWeight = vehicleWeight;
		this.enginePerformance = enginePerformance;
		this.engineEfficiency = engineEfficiency;
		this.stateOfCharge = stateOfCharge;
		this.stateOfChargeRateInflow = stateOfChargeRateInflow;
		this.stateOfChargeRateOutflow = stateOfChargeRateOutflow;
		this.stateOfChargeMinimum = stateOfChargeMinimum;
		this.stateOfChargeMaximum = stateOfChargeMaximum;
		this.vehicleRangeAnxiety = vehicleRangeAnxiety;
		this.chargingStationSelectionRandomness = chargingStationSelectionRandomness;
	}

	// Ports

	// Inputs
	
	public Port<Double> stateOfChargeRateInflowInput = new Port<>();
	
	// Outputs
	
	public Port<Double> positionTraversedLengthOutput = new Port<>();
	public Port<Edge> positionOutput = new Port<>();
	
	public Port<Double> vehicleLengthOutput = new Port<>();
	public Port<Double> speedOutput = new Port<>();
	public Port<Double> stateOfChargeOutput = new Port<>();
	public Port<Double> stateOfChargeRelativeOutput = new Port<>();
	public Port<Double> stateOfChargeMinimumOutput = new Port<>();
	public Port<Double> stateOfChargeMaximumOutput = new Port<>();
	public Port<Double> stateOfChargeRateOutflowOutput = new Port<>();
	public Port<Double> chargingStationConnectionOutput = new Port<>();
	
	public Port<LinkedList<Edge>> positionEdgeListOutput = new Port<>();
	public Port<List<Edge>> pathOutput = new Port<>();
	public Port<Double> speedAbsoluteOutput = new Port<>();
	
	public Port<Edge> nearestChargingStationPositionOutput = new Port<>();
	public Port<Edge> startPositionOutput = new Port<>();
	public Port<Edge> destinationPositionOutput = new Port<>();
	
	public Port<Double> positionEdgeLengthOutput= new Port<>();
	public Port<Double> positionXOutput = new Port<>();
	public Port<Double> positionYOutput = new Port<>();
	public Port<Double> positionZOutput = new Port<>();
	public Port<Double> positionEdgeCapacityOutput = new Port<>();
	public Port<String> positionEdgeTypeOutput = new Port<>();
	public Port<Node> positionTargetNodeOutput = new Port<>();
	public Port<Node> positionSourceNodeOutput = new Port<>();
	
	public Port<Double> shortestPathLengthIndexOutput = new Port<>();
	
	public Port<Boolean> drivingIndicatorOutput = new Port<>();	
	public Port<Double> stateOfChargeCostOutput = new Port<>();
	public Port<Double> timeCostOutput = new Port<>();
	public Port<Double> powerCostOutput = new Port<>();
	public Port<Double> priorityOutput = new Port<>();
	
	public Port<Double> probabilityOutput = new Port<>();
	
	public Port<Boolean> validPositionOutput = new Port<>();
	
	// Constraints
	
	public Constraint chargeStateConstraint = new Constraint(validOutput);
	public Constraint positionConstraint = new Constraint(validPositionOutput);

	// Objectives
	
	// Expressions
	
	public Expression<Edge> positionExpression = new Expression<Edge>(positionOutput, true)	
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return startPosition;	
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
				pathEdges.add(startPositionOutput.get(state, timepoint));
				return pathEdges;
			}
			else 
			{
				// Initialize with initial speed and subtract edge lengths
				double sum = (speedOutput.get(state, timepoint)/(Scenario.MODELSCALE));
				
				// Add already traversed length of edge in timepoint-1
				sum += positionTraversedLengthOutput.get(state, timepoint-1);
				
				List<Edge> pathEdges = pathOutput.get(state, timepoint);
				
				// Create list of reached edges
				LinkedList<Edge> traversedEdges = new LinkedList<Edge>();
				
				// Select every reachable edge and get weight
				for (int i = 0; i < pathEdges.size(); i++) 
				{
					Edge e = pathEdges.get(i);
					traversedEdges.add(e);
								
					if ((sum - e.getWeight()) >= 0) 
					{
						sum -= e.getWeight();
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
				pathEdges.add(startPositionOutput.get(state, timepoint));
				return pathEdges;
			}
			else 
			{
				if (speedOutput.get(state, timepoint) > 0)
				{
					// Generate new random sequence of reachable edges
					return context.generatePath(context.getTargetNode(positionOutput.get(state, timepoint-1).getName()), context.getTargetNode(destinationPositionOutput.get(state, timepoint).getName()));	
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
	
	// Choose speed from 0-1
	public Expression<Double> speedExpression = new Expression<Double>(speedOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorOutput.get(state, timepoint))
			{
				if (!(positionOutput.get(state, timepoint-1).equals(destinationPositionOutput.get(state, timepoint))))
				{
					return (Math.random() < 0.8 ? Math.random() : 0.);
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
	
	// Measures speed through power and crossed distance
	public Expression<Double> speedAbsoluteExpression = new Expression<Double>(speedAbsoluteOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (drivingIndicatorOutput.get(state, timepoint))
			{
				if (!(positionOutput.get(state, timepoint-1).equals(destinationPositionOutput.get(state, timepoint))))
				{
					return (speedOutput.get(state, timepoint)/(Scenario.MODELSCALE));
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
	
	public Expression<Double> positionTraversedLengthExpression = new Expression<Double>(positionTraversedLengthOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return 0.;
			}
			else 
			{
				// Stop
				if ((positionOutput.get(state, timepoint).equals(destinationPositionOutput.get(state, timepoint))) || (speedAbsoluteOutput.get(state, timepoint) == 0))
				{
					return positionTraversedLengthOutput.get(state, timepoint-1);
				}
				// Drive
				else
				{	
					LinkedList<Edge> traversedEdges = positionEdgeListOutput.get(state, timepoint);
					double sum = speedAbsoluteOutput.get(state,timepoint);
					
					// Add already traversed length of edge in timepoint-1
					sum += positionTraversedLengthOutput.get(state, timepoint-1);
					
					// Iterate over all edges in path and add them
					for (int i = 0; i < traversedEdges.size(); i++) 
					{
						Edge e = traversedEdges.get(i);
									
						if ((sum - e.getWeight()) >= 0) 
						{
							sum -= e.getWeight();
						}
						else 
						{
							// Traverse last edge partially, return result
							return sum;
						}
					}
					
				}
			}
			return positionTraversedLengthOutput.get(state, timepoint-1);
		}
	};
	
	public Expression<Double> positionEdgeCapacityExpression = new Expression<Double>(positionEdgeCapacityOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return context.getEdgeWeight(positionOutput.get(state, timepoint).getName());
		}
	};	
	
	public Expression<Double> shortestPathLengthIndexExpression = new Expression<Double>(shortestPathLengthIndexOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			
			List<Edge> shortestPath = context.getShortestPath(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(destinationPosition.getName()));
			
			double shortestPathLengthIndex = 0.;
			
			for (Edge e : shortestPath)
			{
				double differenceX = Math.pow((context.getNode(e.getTarget()).getXPos()-context.getNode(e.getSource()).getXPos()), 2);
				double differenceY = Math.pow((context.getNode(e.getTarget()).getYPos()-context.getNode(e.getSource()).getYPos()), 2);
				
				shortestPathLengthIndex += Math.sqrt(differenceX+differenceY);
			}
			return shortestPathLengthIndex;
		}
	};
	
	public Expression<Double> positionXExpression = new Expression<Double>(positionXOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return (positionSourceNodeOutput.get(state, timepoint).getXPos());
			}
			else 
			{
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{	
					return (positionSourceNodeOutput.get(state, timepoint).getXPos());
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*(positionTargetNodeOutput.get(state, timepoint).getXPos()));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*(positionSourceNodeOutput.get(state, timepoint).getXPos()));
				
					return (term1+term2);
				}
			}
		}
	};
	
	public Expression<Double> positionYExpression = new Expression<Double>(positionYOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return (positionSourceNodeOutput.get(state, timepoint).getYPos());
			}
			else 
			{
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{	
					return (positionSourceNodeOutput.get(state, timepoint).getYPos());
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*(positionTargetNodeOutput.get(state, timepoint).getYPos()));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*(positionSourceNodeOutput.get(state, timepoint).getYPos()));
				
					return (term1+term2);
				}
			}
		}
	};
	
	
	public Expression<Double> positionZExpression = new Expression<Double>(positionZOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return (positionSourceNodeOutput.get(state, timepoint).getZPos());
			}
			else 
			{
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{	
					return (positionSourceNodeOutput.get(state, timepoint).getZPos());
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*(positionTargetNodeOutput.get(state, timepoint).getZPos()));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*(positionSourceNodeOutput.get(state, timepoint).getZPos()));
				
					return (term1+term2);
				}
			}
		}
	};

	
	public Expression<String> positionEdgeTypeExpression = new Expression<String>(positionEdgeTypeOutput)
	{
		@Override protected String evaluate(State state, int timepoint)
		{
			return positionOutput.get(state, timepoint).getTag();
		}
	};
	
	public Expression<Node> positionTargetNodeExpression = new Expression<Node>(positionTargetNodeOutput)
	{
		@Override protected Node evaluate(State state, int timepoint)
		{
			return context.getTargetNode(positionOutput.get(state, timepoint).getName());
		}
	};	
	
	public Expression<Node> positionSourceNodeExpression = new Expression<Node>(positionSourceNodeOutput)
	{
		@Override protected Node evaluate(State state, int timepoint)
		{
			return context.getSourceNode(positionOutput.get(state, timepoint).getName());
		}
	};
	
	
	public Expression<Double> positionEdgeLengthExpression = new Expression<Double>(positionEdgeLengthOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double differenceX = Math.pow((positionTargetNodeOutput.get(state, timepoint).getXPos()-positionSourceNodeOutput.get(state, timepoint).getXPos()), 2);
			double differenceY = Math.pow((positionTargetNodeOutput.get(state, timepoint).getYPos()-positionSourceNodeOutput.get(state, timepoint).getYPos()), 2);
			double differenceZ = Math.pow((positionTargetNodeOutput.get(state, timepoint).getZPos()-positionSourceNodeOutput.get(state, timepoint).getZPos()), 2);
			
			return Math.sqrt(differenceX+differenceY+differenceZ);
		}		
	};
	
	public Expression<Boolean> drivingIndicatorExpression = new Expression<Boolean>(drivingIndicatorOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return (timepoint == 0) ? false : true;		
		}
	};
	
	public Expression<Edge> nearestChargingStationPositionExpression = new Expression<Edge>(nearestChargingStationPositionOutput)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return startPosition;
			}
			else 
			{
				if (chargingStationSelectionRandomness == 0.)
				{
					double minimumCost = Integer.MAX_VALUE;
					Edge result = null;
					
					for (Edge iterator : chargingStationPositions)
					{	
						Double cost = null;
						
						if(context.getExistShortestPath(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(iterator.toString())))
						{
							cost = context.getShortestPathCost(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(iterator.toString()));
							
							//cost += context.getShortestPathCost(context.getTargetNode(iterator.toString()), context.getTargetNode(destinationPosition.getName()));
						}
	
						if (cost != null && cost < minimumCost)
						{
							minimumCost = cost;
							result = iterator;
						}
					}
					
					return result;
				}
				else 
				{
					double minimumCost = Integer.MAX_VALUE;
					Edge result = null;
					
					for (Edge iterator : chargingStationPositions)
					{	
						Double cost = null;
						
						if(context.getExistShortestPath(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(iterator.toString())))
						{
							cost = context.getShortestPathCost(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(iterator.toString()));
							
							//cost += context.getShortestPathCost(context.getTargetNode(iterator.toString()), context.getTargetNode(destinationPosition.getName()));
						}
	
						if (cost != null && cost < minimumCost)
						{
							minimumCost = cost;
							result = iterator;
						}
					}
					
					Edge[] results = {destinationPosition, result};
					
					return results[(int) (Math.random()*results.length)];
				}
			} 
		}
	};
	
	public Expression<Double> vehicleLengthExpression = new Expression<Double>(vehicleLengthOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return vehicleLength;
		}
	};
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{		
			if (speedAbsoluteOutput.get(state, timepoint) > 0.)
			{
				return (-1.-(speedOutput.get(state, timepoint)*5.)+Math.abs(speedOutput.get(state, timepoint-1)*3.));
			}
			else
			{
				if (stateOfChargeRateInflowInput.get(state, timepoint) != null) 
				{
					if (probabilityOutput.get(state, timepoint) > 0.)
					{
						return stateOfChargeRateInflowInput.get(state, timepoint);
					}
					else if (probabilityOutput.get(state, timepoint) < 0.)
					{
						return stateOfChargeRateOutflowOutput.get(state, timepoint);
					}
				}
				return 0.;
			}
		}
	};
	
	public Expression<Double> stateOfChargeExpression = new Expression<Double>(stateOfChargeOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return stateOfCharge;
			}
			else
			{
				if (powerOutput.get(state, timepoint) != 0.) 
				{
					return (stateOfChargeOutput.get(state, timepoint-1)+powerOutput.get(state, timepoint));
					
				}
				else 
				{
					return stateOfChargeOutput.get(state, timepoint-1);
				}
			}
		}
	};
	
	public Expression<Double> stateOfChargeRelativeExpression = new Expression<Double>(stateOfChargeRelativeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (stateOfChargeOutput.get(state, timepoint)/stateOfChargeMaximumOutput.get(state, timepoint));
		}
	};
	
	public Expression<Double> stateOfChargeMaximumExpression = new Expression<Double>(stateOfChargeMaximumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return stateOfChargeMaximum;
		}
	};
	
	public Expression<Double> stateOfChargeMinimumExpression = new Expression<Double>(stateOfChargeMinimumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return stateOfChargeMinimum;
		}
	};
	
	public Expression<Double> stateOfChargeOutflowExpression = new Expression<Double>(stateOfChargeRateOutflowOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 5.;
		}
	};
	
	public Expression<Double> chargingStationConnectionExpression = new Expression<Double>(chargingStationConnectionOutput, true) 
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return ((stateOfChargeRateInflowInput.get(state, timepoint) != null) && (speedOutput.get(state, timepoint) == 0.)) ? 1. : 0.;
		}
	};
	
	public Expression<Edge> startPositionExpression = new Expression<Edge>(startPositionOutput)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			return startPosition;
		}
	};
	
	public Expression<Edge> destinationPositionExpression = new Expression<Edge>(destinationPositionOutput)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return destinationPosition;
			}
			else 
			{
				if (stateOfChargeOutput.get(state, timepoint-1) < stateOfChargeMaximum*vehicleRangeAnxiety)
				{
					return nearestChargingStationPositionOutput.get(state, timepoint-1);		
				}
				else 
				{
					return destinationPosition;	
				}
			}
			
//			if (timepoint == 0)
//			{
//				return destinationPosition;
//			}
//			
//			if ((stateOfChargeOutput.get(state, timepoint-1)/stateOfChargeMaximum) < Math.random()*vehicleRangeAnxiety) 
//			{
//				return nearestChargingStationPositionOutput.get(state, timepoint);	
//			}
//			else 
//			{
//				return destinationPosition;
//			}
		}
	};

	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return stateOfChargeOutput.get(state, timepoint) >= stateOfChargeMinimum && stateOfChargeOutput.get(state, timepoint) <= stateOfChargeMaximum;
		}
	};
	
	public Expression<Double> probabilityExpression = new ConstantNonDeterministicExpression<>(probabilityOutput, 0., -0.25, .25, -0.5, .5, -0.75, .75, -1., 1.);
	
	public Expression<Double> stateOfChargeCostsExpression = new Expression<Double>(stateOfChargeCostOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if ((positionOutput.get(state, timepoint).equals(destinationPosition)) || !(drivingIndicatorOutput.get(state, timepoint)))
			{
				return 0.;
			}
			else 
			{
				return (1.-(stateOfChargeOutput.get(state, timepoint)/stateOfChargeMaximumOutput.get(state, timepoint)));
			}
		}
	};
	
	public Expression<Double> timeCostsExpression = new Expression<Double>(timeCostOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if ((positionOutput.get(state, timepoint).equals(destinationPosition)) || !(drivingIndicatorOutput.get(state, timepoint)))
			{
				return 0.;
			}
			else
			{
				return 1.;
			}
		}
	};
	
	public Expression<Double> powerCostsExpression = new Expression<Double>(powerCostOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			
			if ((positionOutput.get(state, timepoint).equals(destinationPosition)) || !(drivingIndicatorOutput.get(state, timepoint)))
			{
				return 0.;
			}
			else 
			{
				if (powerOutput.get(state, timepoint) < 0)
				{
					return (Math.abs(powerOutput.get(state, timepoint)))/Math.abs(-1.-(speedOutput.get(state, timepoint)*5.));
				}
				else 
				{
					return 0.;
				}
			}
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return ((stateOfChargeCostOutput.get(state, timepoint)*stateOfChargeWeight)+(timeCostOutput.get(state, timepoint)*timeWeight)+(powerCostOutput.get(state, timepoint)*powerWeight));	
		}
	};
	
	public Expression<Double> priorityExpression = new Expression<Double>(priorityOutput, true)	
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return priority;
		}
	};
	
	public Expression<Boolean> validPositionExpression = new Expression<Boolean>(validPositionOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return context.getExistShortestPath(context.getTargetNode(positionOutput.get(state, timepoint).getName()), context.getTargetNode(destinationPositionOutput.get(state, timepoint).getName()));
		}
	};
	
	// Charts
	
	public Chart costChart = new Timeline(costOutput, stateOfChargeCostOutput, timeCostOutput, powerCostOutput);
	public Chart levelChart = new Timeline(stateOfChargeMinimumOutput, stateOfChargeOutput, stateOfChargeMaximumOutput);
	public Chart powerChart = new Timeline(powerOutput);
	public Chart speedChart = new Timeline(speedOutput);
	//public Chart destinationPositionChart = new Histogram<Edge>(destinationPositionOutput);
	public Chart chargingStationConnectionChart = new Timeline(chargingStationConnectionOutput);
}
