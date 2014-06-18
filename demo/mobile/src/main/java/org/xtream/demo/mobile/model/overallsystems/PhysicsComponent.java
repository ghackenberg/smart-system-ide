package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;
import org.xtream.demo.mobile.model.commons.EnergyPhysicsComponent;

public class PhysicsComponent extends EnergyPhysicsComponent
{
	
	@SuppressWarnings("unchecked")
	public PhysicsComponent(int size)
	{
		terminalInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			terminalInputs[i] = new Port<>();
		}
	}
	
	// Inputs
	
	public Port<Double>[] terminalInputs;
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double production = 0;
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(state, timepoint);
				
				production += current > 0. ? current : 0.;
			}
			
			return production;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double consumption = 0;
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(state, timepoint);
				
				consumption += current < 0. ? current : 0.;
			}
			
			return consumption;
		}
	};

}
