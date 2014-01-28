package org.xtream.demo.thermal.model.standard;

import org.xtream.core.model.Expression;
import org.xtream.demo.thermal.model.ThermalComponent;

public class StandardThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new Expression<Boolean>(commandInput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return false;
			}
			else if (temperatureOutput.get(timepoint - 1) + increase > maximumOutput.get(timepoint))
			{
				return true;
			}
			else if (temperatureOutput.get(timepoint - 1) - decrease < minimumOutput.get(timepoint))
			{
				return false;
			}
			else
			{
				return commandInput.get(timepoint - 1);
			}
		}
	};
}
