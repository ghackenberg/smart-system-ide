package org.xtream.demo.hydro.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class WeirDischargeExpression extends Expression<Double>
{
	
	@Constant
	protected double weirDischargeMax;

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
	
	@Constant
	protected Port<Double> currentTurbineDischarge;

	public WeirDischargeExpression(Port<Double> weirDischarge, double weirDischargeMax, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextTurbineDischarge, Port<Double> nextWeirDischarge, Port<Double> currentTurbineDischrage)
	{
		super(weirDischarge);
		
		this.weirDischargeMax = weirDischargeMax;

		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextTurbineDischarge = nextTurbineDischarge;
		this.nextWeirDischarge = nextWeirDischarge;
		
		this.currentTurbineDischarge = currentTurbineDischrage;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (timepoint > 0)
		{
			double inflow = nextTurbineDischarge.get(timepoint) + nextWeirDischarge.get(timepoint);
			
			double minOption = nextLevelMin + inflow * 900 / nextArea - nextLevel.get(timepoint - 1) - currentTurbineDischarge.get(timepoint) * 900 / nextArea;
			double maxOption = nextLevelMax + inflow * 900 / nextArea - nextLevel.get(timepoint - 1) - currentTurbineDischarge.get(timepoint) * 900 / nextArea;
			
			minOption = Math.max(minOption, 0.);
			maxOption = Math.min(maxOption, weirDischargeMax * 900 / nextArea);
			
			minOption = minOption * nextArea / 900;
			maxOption = maxOption * nextArea / 900;
			
			return maxOption > minOption ? minOption + (maxOption - minOption) * Math.random() : 0.;
		}
		else
		{
			return 0.;
		}
	}

}
