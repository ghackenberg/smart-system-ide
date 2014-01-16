package org.xtream.demo.thermal.model.storages;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.components.AbstractLogicsComponent;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class LogicsComponent extends AbstractLogicsComponent
{
	
	// Ports
	
	public Port<Double> commandOutput = new Port<>();
	
	
	// Expressions
	
	public Expression<Double> commandExpression = new ConstantNonDeterministicExpression<Double>(commandOutput, new SetBuilder<Double>().add(-1.).add(0.).add(1.));

}
