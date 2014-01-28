package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.thermal.model.ThermalComponent;

public class SmartThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new ConstantNonDeterministicExpression<Boolean>(commandInput, new SetBuilder<Boolean>().add(true).add(false));
	
}
