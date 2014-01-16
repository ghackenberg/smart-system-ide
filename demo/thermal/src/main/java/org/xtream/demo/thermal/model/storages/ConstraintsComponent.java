package org.xtream.demo.thermal.model.storages;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.components.AbstractConstraintsComponent;

public class ConstraintsComponent extends AbstractConstraintsComponent
{
	
	// Inputs
	
	public Port<Double> levelInput = new Port<>();
	public Port<Double> minimumInput = new Port<>();
	public Port<Double> maximumInput = new Port<>();
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return levelInput.get(timepoint) >= minimumInput.get(timepoint) && levelInput.get(timepoint) <= maximumInput.get(timepoint);
		}
	};

}
