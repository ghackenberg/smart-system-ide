package org.xtream.demo.hydro.model;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class TurbineDischargeExpression extends NonDeterministicExpression<Double>
{
	
	@Constant
	protected Port<Double> nextLevel;
	@Constant
	protected double nextArea;
	@Constant
	protected double nextLevelMax;
	
	@Constant
	protected Port<Double> nextTurbineDischarge;
	@Constant
	protected Port<Double> nextWeirDischarge;
	
	@Constant
	protected Set<Double> options;

	public TurbineDischargeExpression(Port<Double> port,Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextTurbineDischarge, Port<Double> nextWeirDischarge, Set<Double> options)
	{
		super(port);
		
		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMax = nextLevelMax;
		
		this.nextTurbineDischarge = nextTurbineDischarge;
		this.nextWeirDischarge = nextWeirDischarge;
		
		this.options = options;
	}

	@Override
	protected Set<Double> evaluateSet(int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			// Collect valid options
			
			for (double option : options)
			{
				double nextLevelValue = nextLevel.get(timepoint - 1) + option * 900 / nextArea - (nextTurbineDischarge.get(timepoint) + nextWeirDischarge.get(timepoint)) * 900 / nextArea;
				
				if (nextLevelValue >= 0 && nextLevelValue <= nextLevelMax)
				{
					result.add(option);
				}
			}
			
			// Fallback option
			
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
