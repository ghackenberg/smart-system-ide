package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractQualitiesComponent;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;

public class QualitiesComponent extends AbstractQualitiesComponent
{
	public QualitiesComponent(Graph graph) 
	{
		this.graph = graph;
	}
	
	// Parameters
	
	private Graph graph;
	
	// Inputs
	
	public Port<Double> powerInput = new Port<>();
	public Port<Double> chargeStateInput = new Port<>();
	public Port<Edge> positionInput = new Port<>();
	public Port<Edge> destinationPositionInput = new Port<>();
	
	public Port<Double> maximumChargeStateInput = new Port<>();
	public Port<Boolean> targetReachedInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Double> positionCostsOutput = new Port<>();
	public Port<Double> timeCostsOutput = new Port<>();
	public Port<Double> powerCostsOutput = new Port<>();
	public Port<Double> chargeStateCostsOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> positionCostsExpression = new Expression<Double>(positionCostsOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{
			if (targetReachedInput.get(timepoint) || !(drivingIndicatorInput.get(timepoint)))
			{
				return 0.;
			}
			else 
			{
				Double shortestPathCost = graph.getShortestPathCost(graph.getTargetNode(positionInput.get(timepoint).getName()),graph.getTargetNode(destinationPositionInput.get(timepoint).getName()));
				Double longestPathCost = graph.getMaximumPathCost(graph.getTargetNode(positionInput.get(timepoint).getName()),graph.getTargetNode(destinationPositionInput.get(timepoint).getName()));
				return (shortestPathCost/longestPathCost);
			}
		}
	};
	
	public Expression<Double> timeCostsExpression = new Expression<Double>(timeCostsOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{
			if (targetReachedInput.get(timepoint) || !(drivingIndicatorInput.get(timepoint)))
			{
				return 0.;
			}
			else 
			{
				return 1.;
			}
		}
	};
	
	public Expression<Double> powerExpression = new Expression<Double>(powerCostsOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{
			if (targetReachedInput.get(timepoint) || !(drivingIndicatorInput.get(timepoint)))
			{
				return 0.;
			}
			else 
			{
				return powerInput.get(timepoint);
			}
		}
	};
	
	public Expression<Double> chargeStateExpression = new Expression<Double>(chargeStateCostsOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{
			if (targetReachedInput.get(timepoint) || !(drivingIndicatorInput.get(timepoint)))
			{
				return 0.;
			}
			else 
			{
				return (1.0-(chargeStateInput.get(timepoint)/maximumChargeStateInput.get(timepoint)));
			}
		}
	};
	
	
	
}
