package org.xtream.demo.hydro.model;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class WeirDischargeExpression extends NonDeterministicExpression<Double>
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
	protected Port<Double> currentTurbineDischarge;
	
	@Constant
	protected Set<Double> options;

	public WeirDischargeExpression(Port<Double> port, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextTurbineDischarge, Port<Double> nextWeirDischarge, Port<Double> currentTurbineDischrage, Set<Double> options)
	{
		super(port);

		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMax = nextLevelMax;
		
		this.nextTurbineDischarge = nextTurbineDischarge;
		this.nextWeirDischarge = nextWeirDischarge;
		
		this.currentTurbineDischarge = currentTurbineDischrage;
		
		this.options = options;
	}

	@Override
	protected Set<Double> evaluateSet(int timepoint)
	{
		Set<Double> result = new HashSet<>();
		
		if (timepoint > 0)
		{
			for (double option : options)
			{
				double nextLevelValue = nextLevel.get(timepoint - 1) + (currentTurbineDischarge.get(timepoint) + option) * 900 / nextArea - (nextTurbineDischarge.get(timepoint) + nextWeirDischarge.get(timepoint)) * 900 / nextArea;
				
				if (nextLevelValue >= 0 && nextLevelValue <= nextLevelMax)
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
