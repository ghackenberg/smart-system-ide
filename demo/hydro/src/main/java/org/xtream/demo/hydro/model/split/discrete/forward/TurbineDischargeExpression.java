package org.xtream.demo.hydro.model.split.discrete.forward;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class TurbineDischargeExpression extends NonDeterministicExpression<Double>
{
	
	protected Set<Double> turbineDischargeOptions;

	@Reference
	protected Port<Double> previousLevel;
	protected double previousArea;
	protected double previousLevelMin;
	protected double previousLevelMax;

	@Reference
	protected Port<Double> previousTurbineDischarge;
	@Reference
	protected Port<Double> previousWeirDischarge;

	public TurbineDischargeExpression(Port<Double> turbineDischarge, Set<Double> turbineDischargeOptions, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousTurbineDischarge, Port<Double> previousWeirDischarge)
	{
		super(turbineDischarge);
		
		this.turbineDischargeOptions= turbineDischargeOptions;
		
		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousTurbineDischarge = previousTurbineDischarge;
		this.previousWeirDischarge = previousWeirDischarge;
	}

	@Override
	protected Set<Double> evaluateSet(State state, int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			double flow = (previousTurbineDischarge.get(state, timepoint) + previousWeirDischarge.get(state, timepoint) - 0) * 900 / previousArea;
			
			for (double option : turbineDischargeOptions)
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
