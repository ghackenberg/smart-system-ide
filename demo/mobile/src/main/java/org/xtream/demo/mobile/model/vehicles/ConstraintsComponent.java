package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractConstraintsComponent;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.optimizer.State;

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
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return chargeStateInput.get(state, timepoint) >= minimumChargeStateInput.get(state, timepoint) && chargeStateInput.get(state, timepoint) <= maximumChargeStateInput.get(state, timepoint);
		}
	};
	

}
