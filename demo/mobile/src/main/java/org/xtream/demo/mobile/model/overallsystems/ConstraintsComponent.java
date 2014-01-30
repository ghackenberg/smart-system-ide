package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.components.AbstractConstraintsComponent;

public class ConstraintsComponent extends AbstractConstraintsComponent
{
	
	public ConstraintsComponent(double capacity)
	{
		this.capacity = capacity;
	}
	
	// Parameters
	
	private double capacity;
	
	// Inputs
	
	public Port<Double> productionInput = new Port<>();
	public Port<Double> consumptionInput = new Port<>();
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override public Boolean evaluate(int timepoint)
		{
			return productionInput.get(timepoint) >= -capacity && consumptionInput.get(timepoint) <= capacity; 
		}
	};
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);

}
