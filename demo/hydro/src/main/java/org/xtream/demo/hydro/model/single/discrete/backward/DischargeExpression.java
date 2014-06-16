package org.xtream.demo.hydro.model.single.discrete.backward;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class DischargeExpression extends NonDeterministicExpression<Double>
{
	
	protected Set<Double> dischargeOptions;

	@Reference
	protected Port<Double> nextLevel;
	protected double nextArea;
	protected double nextLevelMin;
	protected double nextLevelMax;

	@Reference
	protected Port<Double> nextDischarge;

	public DischargeExpression(Port<Double> discharge, Set<Double> dischargeOptions, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextDischarge)
	{
		super(discharge);
		
		this.dischargeOptions = dischargeOptions;

		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextDischarge = nextDischarge;
	}

	@Override
	protected Set<Double> evaluateSet(int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			double flow = - nextDischarge.get(timepoint) * 900 / nextArea;
			
			for (double option : dischargeOptions)
			{
				double level = nextLevel.get(timepoint - 1) + flow + option * 900 / nextArea;
				
				if (level >= 0 && level <= nextLevelMax)
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
