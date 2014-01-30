package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.components.AbstractConstraintsComponent;

public class ConstraintsComponent extends AbstractConstraintsComponent
{
	
	// Inputs
	
	public Port<Double> chargeStateInput = new Port<>();
	public Port<Double> minimumChargeStateInput = new Port<>();
	public Port<Double> maximumChargeStateInput = new Port<>();

	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Constraints
	
	public Constraint chargeStateConstraint = new Constraint(validOutput);
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return chargeStateInput.get(timepoint) >= minimumChargeStateInput.get(timepoint) && chargeStateInput.get(timepoint) <= maximumChargeStateInput.get(timepoint);
		}
	};
	

}
