package org.xtream.demo.thermal.model.standard;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.thermal.model.SolarComponent;

public class StandardSolarComponent extends SolarComponent
{
	
	public StandardSolarComponent(double scale)
	{
		super(scale);
	}

	// Expressions

	public Expression<Double> efficiencyExpression = new ConstantExpression<Double>(efficiencyInput, 1.);

}
