package org.xtream.demo.thermal.model.solars;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractLogicsComponent;
import org.xtream.core.model.expressions.ConstantExpression;

public class LogicsComponent extends AbstractLogicsComponent
{
	
	// Outputs
	
	public Port<Double> dampingOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> dampingExpression = new ConstantExpression<Double>(dampingOutput, 1.);

}
