package org.xtream.demo.mobile.model.vehicle;

import java.util.LinkedList;
import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.datatypes.Node;

public class ContextComponent extends Component
{
		
	public ContextComponent(Graph graph, String startPosition, String destinationPosition, Double chargeState, Double chargeRate, Double mileage, Double vehicleLength, Double vehicleWidth) 
	{
		this.graph = graph;
		this.startPosition = startPosition;
		this.destinationPosition = destinationPosition;
		this.chargeState = chargeState;
		this.chargeRate = chargeRate;
		this.mileage = mileage;
		this.vehicleLength = vehicleLength;
		this.vehicleWidth = vehicleWidth;
	}
	
	// Parameters
	
	public Graph graph;
	public String startPosition;
	public String destinationPosition;
	public Double chargeState;
	public Double chargeRate;
	public Double mileage;
	public Double vehicleLength;
	public Double vehicleWidth;
	
	// Inputs
	
	public Port<Edge> positionInput = new Port<>();
	public Port<LinkedList<Edge>> positionListInput = new Port<>();
	public Port<Double> speedInput = new Port<>();
	
	// Outputs External/Internal
	
	public Port<Edge> startPositionOutput = new Port<>();
	public Port<Edge> destinationPositionOutput = new Port<>();
	public Port<Edge> positionOutgoingEdgesOutput = new Port<>();
	public Port<Double> positionTraversedLengthOutput = new Port<>();
	public Port<Double> positionEdgeLengthOutput= new Port<>();
	
	public Port<Double> powerOutput = new Port<>();
	
	public Port<Double> chargeStateOutput = new Port<>();
	public Port<Double> chargeStateRelativeOutput = new Port<>();
	public Port<Double> minimumChargeStateOutput = new Port<>();
	public Port<Double> maximumChargeStateOutput = new Port<>();
	
	public Port<Boolean> targetReachedOutput = new Port<>();
	public Port<Boolean> drivingIndicatorOutput = new Port<>();	
	
	// Outputs Internal
	public Port<Double> positionXOutput = new Port<>();
	public Port<Double> positionYOutput = new Port<>();
	public Port<Double> positionZOutput = new Port<>();
	
	public Port<Double> positionEdgeCapacityOutput = new Port<>();
	public Port<String> positionEdgeTypeOutput = new Port<>();
	
	public Port<Node> positionTargetNodeOutput = new Port<>();
	public Port<Node> positionSourceNodeOutput = new Port<>();
	public Port<Double> positionTargetNodeXOutput = new Port<>();
	public Port<Double> positionTargetNodeYOutput = new Port<>();
	public Port<Double> positionTargetNodeZOutput = new Port<>();
	public Port<Double> positionSourceNodeXOutput = new Port<>();
	public Port<Double> positionSourceNodeYOutput = new Port<>();
	public Port<Double> positionSourceNodeZOutput = new Port<>();
	
	public Port<Double> positionAltitudeDifferenceOutput = new Port<>();
	public Port<Double> vehicleLengthOutput = new Port<>();
	public Port<Double> vehicleWidthOutput = new Port<>();
	
	public Port<Double> chargeRateOutput = new Port<>();

    public Port<Double> productionOutput = new Port<>();
    public Port<Double> consumptionOutput = new Port<>();
    public Port<Double> balanceOutput = new Port<>();

	// Expressions
	
	// TODO [Dominik] Utilize Setbuilder (selection) for non deterministic expressions
	public Expression<Edge> positionOutgoingEdgesExpression = new Expression<Edge>(positionOutgoingEdgesOutput, true)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			Set<Edge> set = graph.getOutgoingEdges(positionInput.get(state, timepoint));
			int random = (int) Math.floor(Math.random() * set.size());
			
			for (Edge item : set)
			{
				if (--random < 0)
				{
					return item;
				}
			}
			
			throw new IllegalStateException();
			
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
				if (targetReachedOutput.get(state, timepoint) || (speedInput.get(state, timepoint) == 0))
				{
					return positionTraversedLengthOutput.get(state, timepoint-1);
				}
				// Drive
				else
				{	
					LinkedList<Edge> traversedEdges = positionListInput.get(state, timepoint);
					double sum = speedInput.get(state,timepoint);
					
					// Add already traversed length of edge in timepoint-1
					sum += positionTraversedLengthOutput.get(state, timepoint-1);
					
					// Iterate over all edges in path and add them
					for (int i = 0; i < traversedEdges.size(); i++) {
						Edge e = traversedEdges.get(i);
									
						if ((sum - Double.parseDouble(e.getWeight())) >= 0) 
						{
							sum -= Double.parseDouble(e.getWeight());
						}
						else {
							// Traverse last edge partially, return result
							return sum;
						}
					}
					
				}
			}
			return positionTraversedLengthOutput.get(state, timepoint-1);
		}
	};
	
	public Expression<Double> positionXExpression = new Expression<Double>(positionXOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0) 
			{
				return positionSourceNodeXOutput.get(state, timepoint);
			}
			else 
			{
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{	
					return positionSourceNodeXOutput.get(state, timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*positionTargetNodeXOutput.get(state, timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*positionSourceNodeXOutput.get(state, timepoint));
				
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
				return positionSourceNodeYOutput.get(state, timepoint);
			}
			else 
			{			
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{
					return positionSourceNodeYOutput.get(state, timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*positionTargetNodeYOutput.get(state, timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*positionSourceNodeYOutput.get(state, timepoint));
				
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
				return positionSourceNodeZOutput.get(state, timepoint);
			}
			else 
			{
				if (positionEdgeLengthOutput.get(state, timepoint) == 0.0)
				{
					return positionSourceNodeZOutput.get(state, timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint))*positionTargetNodeZOutput.get(state, timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(state, timepoint)/positionEdgeLengthOutput.get(state, timepoint)))*positionSourceNodeZOutput.get(state, timepoint));
				
					return (term1+term2);
				}
			}
		}
	};	
	
	public Expression<Double> positionEdgeCapacityExpression = new Expression<Double>(positionEdgeCapacityOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return graph.getEdgeWeight(positionInput.get(state, timepoint).getName());
		}
	};	
	
	public Expression<String> positionEdgeTypeExpression = new Expression<String>(positionEdgeTypeOutput)
	{
		@Override protected String evaluate(State state, int timepoint)
		{
			return positionInput.get(state, timepoint).getTag();
		}
	};
	
	public Expression<Double> positionAltitudeDifferenceExpression = new Expression<Double>(positionAltitudeDifferenceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (positionTargetNodeZOutput.get(state, timepoint)-positionSourceNodeZOutput.get(state, timepoint));
		}
	};
	
	public Expression<Node> positionTargetNodeExpression = new Expression<Node>(positionTargetNodeOutput)
	{
		@Override protected Node evaluate(State state, int timepoint)
		{
			return graph.getTargetNode(positionInput.get(state, timepoint).getName());
		}
	};	
	
	public Expression<Node> positionSourceNodeExpression = new Expression<Node>(positionSourceNodeOutput)
	{
		@Override protected Node evaluate(State state, int timepoint)
		{
			return graph.getSourceNode(positionInput.get(state, timepoint).getName());
		}
	};
	
	public Expression<Double> positionTargetNodeXExpression = new Expression<Double>(positionTargetNodeXOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(state, timepoint).getXpos());
		}
	};
	
	public Expression<Double> positionTargetNodeYExpression = new Expression<Double>(positionTargetNodeYOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(state, timepoint).getYpos());
		}
	};
	
	public Expression<Double> positionTargetNodeZExpression = new Expression<Double>(positionTargetNodeZOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(state, timepoint).getWeight());
		}
	};
	
	public Expression<Double> positionSourceNodeXExpression = new Expression<Double>(positionSourceNodeXOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(state, timepoint).getXpos());
		}
	};
	
	public Expression<Double> positionSourceNodeYExpression = new Expression<Double>(positionSourceNodeYOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(state, timepoint).getYpos());
		}
	};
	
	public Expression<Double> positionSourceNodeZExpression = new Expression<Double>(positionSourceNodeZOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(state, timepoint).getWeight());
		}
	};
	
	public Expression<Double> positionEdgeLengthExpression = new Expression<Double>(positionEdgeLengthOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double differenceX = Math.pow((positionTargetNodeXOutput.get(state, timepoint)-positionSourceNodeXOutput.get(state, timepoint)), 2);
			double differenceY = Math.pow((positionTargetNodeYOutput.get(state, timepoint)-positionSourceNodeYOutput.get(state, timepoint)), 2);
			double differenceZ = Math.pow((positionTargetNodeZOutput.get(state, timepoint)-positionSourceNodeZOutput.get(state, timepoint)), 2);
			
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
	
	public Expression<Boolean> targetReachedExpression = new Expression<Boolean>(targetReachedOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return (positionInput.get(state, timepoint).equals(destinationPositionOutput.get(state, timepoint)));
		}
	};
	
	public Expression<Edge> startPositionExpression = new Expression<Edge>(startPositionOutput)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			return graph.getEdge(startPosition);
		}
	};
	
	public Expression<Edge> destinationPositionExpression = new Expression<Edge>(destinationPositionOutput)
	{
		@Override protected Edge evaluate(State state, int timepoint)
		{
			return graph.getEdge(destinationPosition);
		}
	};
	
	public Expression<Double> vehicleLengthExpression = new Expression<Double>(vehicleLengthOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return vehicleLength;
		}
	};
	
	public Expression<Double> vehicleWidthExpression = new Expression<Double>(vehicleWidthOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return vehicleWidth;
		}
	};
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{		
			if (speedInput.get(state, timepoint) > 0)
			{
				
				double slope;
				if (Math.abs(positionAltitudeDifferenceOutput.get(state, timepoint)+positionAltitudeDifferenceOutput.get(state, timepoint)) < (Math.pow(speedInput.get(state, timepoint),2)))
				{
					slope = (Math.pow(speedInput.get(state, timepoint), 2))+positionAltitudeDifferenceOutput.get(state, timepoint)+positionAltitudeDifferenceOutput.get(state, timepoint); 
				}
				else {
					slope = 0.;
				}
				
				return (mileage*10*(slope*0.3));
			}
			else
			{
				return 0.;
			}
		}
	};
	
	public Expression<Double> chargeStateExpression = new Expression<Double>(chargeStateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 84.0;
			}
			else
			{
				// Charge
				if (targetReachedOutput.get(state, timepoint) || !(drivingIndicatorOutput.get(state, timepoint)))
				{
					if ((chargeStateOutput.get(state, timepoint-1)+chargeRateOutput.get(state, timepoint)) > maximumChargeStateOutput.get(state, timepoint))
					{
						return maximumChargeStateOutput.get(state, timepoint);
					}
					else
					{
						return (chargeStateOutput.get(state, timepoint-1)+chargeRateOutput.get(state, timepoint));
					}
				}
				// Discharge
				else
				{
					return (chargeStateOutput.get(state, timepoint-1)-powerOutput.get(state, timepoint));
				}
			}
		}
	};
	
	public Expression<Double> chargeStateRelativeExpression = new Expression<Double>(chargeStateRelativeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (chargeStateOutput.get(state, timepoint)/maximumChargeStateOutput.get(state, timepoint));
		}
	};
	
	public Expression<Double> chargeRateExpression = new Expression<Double>(chargeRateOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return chargeRate;
		}
	};

	public Expression<Double> maximumChargeStateExpression = new Expression<Double>(maximumChargeStateOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return chargeState;
		}
	};
	
	public Expression<Double> minimumChargeStateExpression = new ConstantExpression<Double>(minimumChargeStateOutput, 0.);
	
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);
	public Expression<Double> productionExpression = new ConstantExpression<Double>(productionOutput, 0.);
	public Expression<Double> balanceExpression = new ConstantExpression<Double>(balanceOutput, 0.);

}
