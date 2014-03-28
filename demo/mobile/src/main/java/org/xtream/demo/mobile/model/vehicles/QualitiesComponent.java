package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractQualitiesComponent;

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
	public Port<Edge> positionTargetInput = new Port<>();
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
			Double shortestPathCostInitial = graph.getShortestPathCost(graph.getTargetNode(positionInput.get(0).getName()),graph.getTargetNode(destinationPositionInput.get(0).getName()));
			Double shortestPathCost = graph.getShortestPathCost(graph.getTargetNode(positionInput.get(timepoint).getName()),graph.getTargetNode(destinationPositionInput.get(timepoint).getName()));
			Double longestPathCost = graph.getLongestPathCost(graph.getTargetNode(positionInput.get(timepoint).getName()),graph.getTargetNode(destinationPositionInput.get(timepoint).getName()));
			
			if (targetReachedInput.get(timepoint) || !(drivingIndicatorInput.get(timepoint)))
			{
				return 0.;
			}
			else if (shortestPathCost == 0 && longestPathCost == 0)
			{
				return 0.;
			}
			else if (positionTargetInput.get(timepoint).equals(graph.getShortestPath(graph.getTargetNode(positionInput.get(timepoint).getName()),graph.getTargetNode(destinationPositionInput.get(timepoint).getName())).get(0)))
			{
				return 0.;
			}
			else 
			{
				return (1.0*(shortestPathCost/shortestPathCostInitial));
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
			else if (powerInput.get(timepoint) == 0)
			{
				return 0.;
			}
			else 
			{
				return (powerInput.get(timepoint)/3264);
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
