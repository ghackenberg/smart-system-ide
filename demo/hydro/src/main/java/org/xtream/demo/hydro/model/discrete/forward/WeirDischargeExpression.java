package org.xtream.demo.hydro.model.discrete.forward;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class WeirDischargeExpression extends NonDeterministicExpression<Double>
{
	
	@Constant
	protected Set<Double> weirDischargeOptions;

	@Constant
	protected Port<Double> previousLevel;
	@Constant
	protected double previousArea;
	@Constant
	protected double previousLevelMin;
	@Constant
	protected double previousLevelMax;
	
	@Constant
	protected Port<Double> previousTurbineDischarge;
	@Constant
	protected Port<Double> previousWeirDischarge;
	
	@Constant
	protected Port<Double> currentTurbineDischarge;

	public WeirDischargeExpression(Port<Double> weirDischarge, Set<Double> weirDischargeOptions, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousTurbineDischarge, Port<Double> previousWeirDischarge, Port<Double> currentTurbineDischrage)
	{
		super(weirDischarge);
		
		this.weirDischargeOptions = weirDischargeOptions;

		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousTurbineDischarge = previousTurbineDischarge;
		this.previousWeirDischarge = previousWeirDischarge;
		
		this.currentTurbineDischarge = currentTurbineDischrage;
	}

	@Override
	protected Set<Double> evaluateSet(int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			double flow = (previousTurbineDischarge.get(timepoint) + previousWeirDischarge.get(timepoint) - currentTurbineDischarge.get(timepoint)) * 900 / previousArea;
			
			for (double option : weirDischargeOptions)
			{
				double level = previousLevel.get(timepoint - 1) + flow - option * 900 / previousArea;
				
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
