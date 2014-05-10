package org.xtream.demo.mobile.model.vehicles;

import java.util.Set;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.datatypes.Node;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.mobile.model.commons.EnergyPhysicsComponent;

public class PhysicsComponent extends EnergyPhysicsComponent
{
		
	public PhysicsComponent(Graph graph, String startPosition, String destinationPosition) 
	{
		this.graph = graph;
		this.startPosition = startPosition;
		this.destinationPosition = destinationPosition;
	}
	
	// Parameters
	
	private Graph graph;
	private String startPosition;
	private String destinationPosition;
	
	// Inputs
	
	public Port<Edge> positionInput = new Port<>();
	public Port<Double> speedInput = new Port<>();
	
	// Outputs External/Internal
	
	public Port<Edge> startPositionOutput = new Port<>();
	public Port<Edge> destinationPositionOutput = new Port<>();
	public Port<Edge> positionOutgoingEdgesOutput = new Port<>();
	public Port<Double> positionTraversedLengthOutput = new Port<>();
	public Port<Double> positionEdgeLengthOutput= new Port<>();
	
	public Port<Double> powerOutput= new Port<>();
	
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
	
	// Expressions
	
	// TODO [Dominik] Utilize Setbuilder (selection) for non deterministic expressions
	public Expression<Edge> positionOutgoingEdgesExpression = new Expression<Edge>(positionOutgoingEdgesOutput)
	{
		@Override public Edge evaluate(int timepoint)
		{
			Set<Edge> set = graph.getOutgoingEdges(positionInput.get(timepoint));
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
	
	public Expression<Double> positionTraversedLengthExpression = new Expression<Double>(positionTraversedLengthOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0) 
			{
				return 0.;
			}
			else 
			{
				// Stop
				if (targetReachedOutput.get(timepoint) || (speedInput.get(timepoint) == 0))
				{
					return positionTraversedLengthOutput.get(timepoint-1);
				}
				// Drive
				else
				{
					if (positionTraversedLengthOutput.get(timepoint-1) >= positionEdgeLengthOutput.get(timepoint-1))
					{
						return (speedInput.get(timepoint)+(positionTraversedLengthOutput.get(timepoint-1)-positionEdgeLengthOutput.get(timepoint-1)));
					}
					else 
					{
						return (speedInput.get(timepoint)+positionTraversedLengthOutput.get(timepoint-1));
					}
				}
			}
		}
	};
	
	public Expression<Double> positionXExpression = new Expression<Double>(positionXOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0) 
			{
				return positionSourceNodeXOutput.get(timepoint);
			}
			else 
			{
				if (positionEdgeLengthOutput.get(timepoint) == 0.0)
				{	
					return positionSourceNodeXOutput.get(timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint))*positionTargetNodeXOutput.get(timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint)))*positionSourceNodeXOutput.get(timepoint));
				
					return (term1+term2);
				}
			}
		}
	};
	
	public Expression<Double> positionYExpression = new Expression<Double>(positionYOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0) 
			{
				return positionSourceNodeYOutput.get(timepoint);
			}
			else 
			{			
				if (positionEdgeLengthOutput.get(timepoint) == 0.0)
				{
					return positionSourceNodeYOutput.get(timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint))*positionTargetNodeYOutput.get(timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint)))*positionSourceNodeYOutput.get(timepoint));
				
					return (term1+term2);
				}
			}
		}
	};	
	
	public Expression<Double> positionZExpression = new Expression<Double>(positionZOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0) 
			{
				return positionSourceNodeZOutput.get(timepoint);
			}
			else 
			{
				if (positionEdgeLengthOutput.get(timepoint) == 0.0)
				{
					return positionSourceNodeZOutput.get(timepoint);
				}
				else 
				{
					double term1 = ((positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint))*positionTargetNodeZOutput.get(timepoint));
					double term2 = ((1.0-(positionTraversedLengthOutput.get(timepoint)/positionEdgeLengthOutput.get(timepoint)))*positionSourceNodeZOutput.get(timepoint));
				
					return (term1+term2);
				}
			}
		}
	};	
	
	public Expression<Double> positionEdgeCapacityExpression = new Expression<Double>(positionEdgeCapacityOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return graph.getEdgeWeight(positionInput.get(timepoint).getName());
		}
	};	
	
	public Expression<String> positionEdgeTypeExpression = new Expression<String>(positionEdgeTypeOutput)
	{
		@Override public String evaluate(int timepoint)
		{
			return positionInput.get(timepoint).getTag();
		}
	};
	
	public Expression<Double> positionAltitudeDifferenceExpression = new Expression<Double>(positionAltitudeDifferenceOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (positionTargetNodeZOutput.get(timepoint)-positionSourceNodeZOutput.get(timepoint));
		}
	};
	
	public Expression<Node> positionTargetNodeExpression = new Expression<Node>(positionTargetNodeOutput)
	{
		@Override public Node evaluate(int timepoint)
		{
			return graph.getTargetNode(positionInput.get(timepoint).getName());
		}
	};	
	
	public Expression<Node> positionSourceNodeExpression = new Expression<Node>(positionSourceNodeOutput)
	{
		@Override public Node evaluate(int timepoint)
		{
			return graph.getSourceNode(positionInput.get(timepoint).getName());
		}
	};
	
	public Expression<Double> positionTargetNodeXExpression = new Expression<Double>(positionTargetNodeXOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(timepoint).getXpos());
		}
	};
	
	public Expression<Double> positionTargetNodeYExpression = new Expression<Double>(positionTargetNodeYOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(timepoint).getYpos());
		}
	};
	
	public Expression<Double> positionTargetNodeZExpression = new Expression<Double>(positionTargetNodeZOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionTargetNodeOutput.get(timepoint).getWeight());
		}
	};
	
	public Expression<Double> positionSourceNodeXExpression = new Expression<Double>(positionSourceNodeXOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(timepoint).getXpos());
		}
	};
	
	public Expression<Double> positionSourceNodeYExpression = new Expression<Double>(positionSourceNodeYOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(timepoint).getYpos());
		}
	};
	
	public Expression<Double> positionSourceNodeZExpression = new Expression<Double>(positionSourceNodeZOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Double.parseDouble(positionSourceNodeOutput.get(timepoint).getWeight());
		}
	};
	
	public Expression<Double> positionEdgeLengthExpression = new Expression<Double>(positionEdgeLengthOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double differenceX = Math.pow((positionTargetNodeXOutput.get(timepoint)-positionSourceNodeXOutput.get(timepoint)), 2);
			double differenceY = Math.pow((positionTargetNodeYOutput.get(timepoint)-positionSourceNodeYOutput.get(timepoint)), 2);
			double differenceZ = Math.pow((positionTargetNodeZOutput.get(timepoint)-positionSourceNodeZOutput.get(timepoint)), 2);
			
			return Math.sqrt(differenceX+differenceY+differenceZ);
		}		
	};
	
	public Expression<Boolean> drivingIndicatorExpression = new Expression<Boolean>(drivingIndicatorOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return (timepoint == 0) ? false : true;		
		}
	};
	
	public Expression<Boolean> targetReachedExpression = new Expression<Boolean>(targetReachedOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return (positionInput.get(timepoint).equals(destinationPositionOutput.get(timepoint)));
		}
	};
	
	public Expression<Edge> startPositionExpression = new Expression<Edge>(startPositionOutput)
	{
		@Override public Edge evaluate(int timepoint)
		{
			return graph.getEdge(startPosition);
		}
	};
	
	public Expression<Edge> destinationPositionExpression = new Expression<Edge>(destinationPositionOutput)
	{
		@Override public Edge evaluate(int timepoint)
		{
			return graph.getEdge(destinationPosition);
		}
	};
	
	public Expression<Double> vehicleLengthExpression = new ConstantExpression<Double>(vehicleLengthOutput, 0.003);
	
	public Expression<Double> vehicleWidthExpression = new ConstantExpression<Double>(vehicleWidthOutput, 0.002);
	
	
	public Expression<Double> powerExpression = new Expression<Double>(powerOutput)
	{
		@Override public Double evaluate(int timepoint)
		{		
			if (speedInput.get(timepoint) > 0)
			{
				double milage = 0.2353;
				
				double slope;
				if (Math.abs(positionAltitudeDifferenceOutput.get(timepoint)+positionAltitudeDifferenceOutput.get(timepoint)) < (Math.pow(speedInput.get(timepoint),2)))
				{
					slope = (Math.pow(speedInput.get(timepoint), 2))+positionAltitudeDifferenceOutput.get(timepoint)+positionAltitudeDifferenceOutput.get(timepoint); 
				}
				else {
					slope = 0.;
				}
				
				return (milage*(slope*0.3));
			}
			else
			{
				return 0.;
			}
		}
	};
	
	public Expression<Double> chargeStateExpression = new Expression<Double>(chargeStateOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 85.00;
			}
			else
			{
				// Charge
				if (targetReachedOutput.get(timepoint) || !(drivingIndicatorOutput.get(timepoint)))
				{
					if ((chargeStateOutput.get(timepoint-1)+chargeRateOutput.get(timepoint)) > maximumChargeStateOutput.get(timepoint))
					{
						return maximumChargeStateOutput.get(timepoint);
					}
					else
					{
						return (chargeStateOutput.get(timepoint-1)+chargeRateOutput.get(timepoint));
					}
				}
				// Discharge
				else
				{
					return (chargeStateOutput.get(timepoint-1)-powerOutput.get(timepoint));
				}
			}
		}
	};
	
	public Expression<Double> chargeStateRelativeExpression = new Expression<Double>(chargeStateRelativeOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (chargeStateOutput.get(timepoint)/maximumChargeStateOutput.get(timepoint));
		}
	};
	
	public Expression<Double> chargeRateExpression = new ConstantExpression<Double>(chargeRateOutput, 5.67);	
	public Expression<Double> minimumChargeStateExpression = new ConstantExpression<Double>(minimumChargeStateOutput, 0.);
	public Expression<Double> maximumChargeStateExpression = new ConstantExpression<Double>(maximumChargeStateOutput, 85.0);
	
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);
	public Expression<Double> productionExpression = new ConstantExpression<Double>(productionOutput, 0.);

}
