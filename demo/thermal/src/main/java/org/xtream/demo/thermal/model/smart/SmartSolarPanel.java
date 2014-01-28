package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.thermal.model.SolarComponent;

public class SmartSolarPanel extends SolarComponent
{
	
	public SmartSolarPanel(double scale)
	{
		super(scale);
	}

	// Expressions

	public Expression<Double> dampingExpression = new ConstantExpression<Double>(efficiencyInput, 1.);

}
