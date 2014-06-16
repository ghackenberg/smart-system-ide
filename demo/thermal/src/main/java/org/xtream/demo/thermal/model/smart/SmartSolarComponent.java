package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.thermal.model.SolarComponent;

public class SmartSolarComponent extends SolarComponent
{
	
	public SmartSolarComponent(double scale)
	{
		super(scale);
	}

	// Expressions

	public Expression<Double> dampingExpression = new ConstantNonDeterministicExpression<Double>(efficiencyInput, 0., .5, 1.);

}
