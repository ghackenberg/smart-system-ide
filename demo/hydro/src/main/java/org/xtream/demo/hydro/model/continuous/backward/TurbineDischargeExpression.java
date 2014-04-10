package org.xtream.demo.hydro.model.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	@Constant
	protected double turbineDischargeMax;
	
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
