package org.xtream.demo.hydro.model.split.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	protected double turbineDischargeMax;

	@Reference
	protected Port<Double> nextLevel;
	protected double nextArea;
	protected double nextLevelMin;
	protected double nextLevelMax;

	@Reference
	protected Port<Double> nextTurbineDischarge;
	@Reference
	protected Port<Double> nextWeirDischarge;

	public TurbineDischargeExpression(Port<Double> turbineDischarge, double turbineDischargeMax, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextTurbineDischarge, Port<Double> nextWeirDischarge)
	{
		super(turbineDischarge);
		
		this.turbineDischargeMax = turbineDischargeMax;
		
		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextTurbineDischarge = nextTurbineDischarge;
		this.nextWeirDischarge = nextWeirDischarge;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (timepoint > 0)
		{
			double outflow = nextTurbineDischarge.get(timepoint) + nextWeirDischarge.get(timepoint);
			double inflow = 0;
			
			double minOption = nextLevelMin + outflow * 900 / nextArea - nextLevel.get(timepoint - 1) - inflow * 900 / nextArea;
			double maxOption = nextLevelMax + outflow * 900 / nextArea - nextLevel.get(timepoint - 1) - inflow * 900 / nextArea;
			
			minOption = Math.max(minOption, 0.);
			maxOption = Math.min(maxOption, turbineDischargeMax * 900 / nextArea);
			
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
