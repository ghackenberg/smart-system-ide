package org.xtream.demo.hydro.model.split.discrete.backward;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class TurbineDischargeExpression extends NonDeterministicExpression<Double>
{
	
	@Constant
	protected Set<Double> turbineDischargeOptions;
	
	@Constant
	protected Port<Double> nextLevel;
	@Constant
	protected double nextArea;
	@Constant
	protected double nextLevelMin;
	@Constant
	protected double nextLevelMax;
	
	@Constant
	protected Port<Double> nextTurbineDischarge;
	@Constant
	protected Port<Double> nextWeirDischarge;

	public TurbineDischargeExpression(Port<Double> turbineDischarge, Set<Double> turbineDischargeOptions, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextTurbineDischarge, Port<Double> nextWeirDischarge)
	{
		super(turbineDischarge);
		
		this.turbineDischargeOptions = turbineDischargeOptions;
		
		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextTurbineDischarge = nextTurbineDischarge;
		this.nextWeirDischarge = nextWeirDischarge;
	}

	@Override
	protected Set<Double> evaluateSet(int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			double flow = (0 - nextTurbineDischarge.get(timepoint) - nextWeirDischarge.get(timepoint)) * 900 / nextArea;
			
			for (double option : turbineDischargeOptions)
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
