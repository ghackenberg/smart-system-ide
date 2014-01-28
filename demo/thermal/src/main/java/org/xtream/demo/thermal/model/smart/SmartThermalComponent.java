package org.xtream.demo.thermal.model.smart;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.NonDeterministicExpression;
import org.xtream.demo.thermal.model.ThermalComponent;

public class SmartThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new NonDeterministicExpression<Boolean>(commandInput)
	{
		@Override protected Set<Boolean> evaluateSet(int timepoint)
		{
			Set<Boolean> result = new HashSet<>();
			
			if (timepoint == 0 || temperatureOutput.get(timepoint - 1) + increase <= maximumOutput.get(timepoint))
			{
				result.add(false);
			}
			if (timepoint == 0 || temperatureOutput.get(timepoint - 1) - decrease >= minimumOutput.get(timepoint))
			{
				result.add(true);
			}
			
			return result;
		}
		
	};
	
}
