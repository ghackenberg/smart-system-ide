package org.xtream.demo.thermal.model.solars;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractCostsComponent;
import org.xtream.core.model.expressions.ConstantExpression;

public class CostsComponent extends AbstractCostsComponent
{
	
	// Inputs
	
	public Port<Double> dampingInput = new Port<>();
	
	// Expressions
	
	public Expression<Double> costsExpression = new ConstantExpression<Double>(costsOutput, 0.);

}
