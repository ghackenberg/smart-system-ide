package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.demo.thermal.model.ThermalComponent;

public class SmartThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new Expression<Boolean>(commandInput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			if (timepoint != 0 && temperatureOutput.get(timepoint - 1) + increase > maximumOutput.get(timepoint))
			{
				return true;
			}
			else if (timepoint != 0 && temperatureOutput.get(timepoint - 1) - decrease < minimumOutput.get(timepoint))
			{
				return false;
			}
			else
			{
				return Math.random() <= probabilityInput.get(timepoint) ? true : false;
			}
		}
		
	};
	
}
