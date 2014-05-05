package org.xtream.demo.mobile.model.vehicles;

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
	
	@SuppressWarnings("unused")
	private Graph graph;
	
	// Inputs
	
	public Port<Double> powerInput = new Port<>();
	
	public Port<Boolean> targetReachedInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Double> timeCostsOutput = new Port<>();
	public Port<Double> powerCostsOutput = new Port<>();
	
	// Expressions
	
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
				return (powerInput.get(timepoint)/5.);
			}
		}
	};
	
}
