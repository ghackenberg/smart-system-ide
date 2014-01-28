package org.xtream.demo.thermal.model.standard;

import org.xtream.core.model.Expression;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.thermal.model.SolarComponent;

public class StandardSolarPanel extends SolarComponent
{
	
	public StandardSolarPanel(double scale)
	{
		super(scale);
	}

	// Expressions

	public Expression<Double> dampingExpression = new ConstantNonDeterministicExpression<Double>(efficiencyInput, new SetBuilder<Double>().add(0.).add(.5).add(1.));

}
