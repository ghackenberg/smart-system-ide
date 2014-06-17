package org.xtream.demo.hydro.model.single.discrete.forward;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.expressions.NonDeterministicExpression;
import org.xtream.core.optimizer.State;

public class DischargeExpression extends NonDeterministicExpression<Double>
{
	
	protected Set<Double> dischargeOptions;

	@Reference
	protected Port<Double> previousLevel;
	protected double previousArea;
	protected double previousLevelMin;
	protected double previousLevelMax;

	@Reference
	protected Port<Double> previousDischarge;

	public DischargeExpression(Port<Double> discharge, Set<Double> dischargeOptions, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousDischarge)
	{
		super(discharge);
		
		this.dischargeOptions = dischargeOptions;

		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousDischarge = previousDischarge;
	}

	@Override
	protected Set<Double> evaluateSet(State state, int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			double flow = previousDischarge.get(state, timepoint) * 900 / previousArea;
			
			for (double option : dischargeOptions)
			{
				double level = previousLevel.get(state, timepoint - 1) + flow - option * 900 / previousArea;
				
				if (level >= 0 && level <= previousLevelMax)
				{
					result.add(option);
				}
			}
			
			if (result.size() == 0)
			{
				result.add(0.);
			}
		}
		else
		{
			result.add(0.);
		}
		
		return result;
	}

}
