package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.thermal.model.StorageComponent;

public class SmartStorageComponent extends StorageComponent
{

	public SmartStorageComponent(double speed, double capacity)
	{
		super(speed, capacity);
	}

	// Expressions
	
	public Expression<Double> commandExpression = new ConstantNonDeterministicExpression<Double>(commandInput, new SetBuilder<Double>().add(-1.).add(0.).add(1.));
	
}
