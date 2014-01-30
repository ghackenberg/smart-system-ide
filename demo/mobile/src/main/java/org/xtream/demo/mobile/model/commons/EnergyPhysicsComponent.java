package org.xtream.demo.mobile.model.commons;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractPhysicsComponent;

public abstract class EnergyPhysicsComponent extends AbstractPhysicsComponent
{
	
	// Ports
	
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return productionOutput.get(timepoint) + consumptionOutput.get(timepoint);
		}
	};

}
