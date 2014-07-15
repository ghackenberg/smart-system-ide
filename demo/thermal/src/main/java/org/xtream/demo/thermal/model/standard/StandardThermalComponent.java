package org.xtream.demo.thermal.model.standard;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.thermal.model.ThermalComponent;

public class StandardThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new Expression<Boolean>(commandInput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return false;
			}
			else if (temperatureOutput.get(state, timepoint - 1) + increase > maximumOutput.get(state, timepoint))
			{
				return true;
			}
			else if (temperatureOutput.get(state, timepoint - 1) - decrease < minimumOutput.get(state, timepoint))
			{
				return false;
			}
			else
			{
				return commandInput.get(state, timepoint - 1);
			}
		}
	};
}
