package org.xtream.demo.mobile.model.commons;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractPhysicsComponent;
import org.xtream.core.optimizer.State;

public abstract class EnergyPhysicsComponent extends AbstractPhysicsComponent
{
	
	// Ports
	
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return productionOutput.get(state, timepoint) + consumptionOutput.get(state, timepoint);
		}
	};

}
