package org.xtream.demo.thermal.model.nets;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.demo.thermal.model.commons.EnergyPhysicsComponent;

public class PhysicsComponent extends EnergyPhysicsComponent
{
	
	@SuppressWarnings("unchecked")
	public PhysicsComponent(int size)
	{
		terminalInputs = new Port[size + 2];
		
		for (int i = 0; i < size + 2; i++)
		{
			terminalInputs[i] = new Port<>();
		}
	}
	
	// Inputs
	
	public Port<Double>[] terminalInputs;
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double production = 0;
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(timepoint);
				
				production += current > 0. ? current : 0.;
			}
			
			return production;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double consumption = 0;
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(timepoint);
				
				consumption += current < 0. ? current : 0.;
			}
			
			return consumption;
		}
	};

}
