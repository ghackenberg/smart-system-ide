package org.xtream.demo.thermal.model.thermals;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.components.AbstractLogicsComponent;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class LogicsComponent extends AbstractLogicsComponent
{
	
	// Outputs
	
	public Port<Boolean> commandOutput = new Port<>();
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new ConstantNonDeterministicExpression<Boolean>(commandOutput, new SetBuilder<Boolean>().add(true).add(false));

}
