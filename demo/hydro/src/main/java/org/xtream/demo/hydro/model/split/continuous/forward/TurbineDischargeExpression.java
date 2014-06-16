package org.xtream.demo.hydro.model.split.continuous.forward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	protected double turbineDischargeMax;

	@Reference
	protected Port<Double> previousLevel;
	protected double previousArea;
	protected double previousLevelMin;
	protected double previousLevelMax;

	@Reference
	protected Port<Double> previousTurbineDischarge;
	@Reference
	protected Port<Double> previousWeirDischarge;

	public TurbineDischargeExpression(Port<Double> turbineDischarge, double turbineDischargeMax, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousTurbineDischarge, Port<Double> previousWeirDischarge)
	{
		super(turbineDischarge);
		
		this.turbineDischargeMax = turbineDischargeMax;
		
		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousTurbineDischarge = previousTurbineDischarge;
		this.previousWeirDischarge = previousWeirDischarge;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (timepoint > 0)
		{
			double outflow = 0;
			double inflow = previousTurbineDischarge.get(timepoint) + previousWeirDischarge.get(timepoint);
			
			double minOption = previousLevelMin + outflow * 900 / previousArea - previousLevel.get(timepoint - 1) - inflow * 900 / previousArea;
			double maxOption = previousLevelMax + outflow * 900 / previousArea - previousLevel.get(timepoint - 1) - inflow * 900 / previousArea;
			
			minOption = Math.max(minOption, 0.);
			maxOption = Math.min(maxOption, turbineDischargeMax * 900 / previousArea);
			
			minOption = minOption * previousArea / 900;
			maxOption = maxOption * previousArea / 900;
			
			return maxOption > minOption ? minOption + (maxOption - minOption) * Math.random() : 0.;
		}
		else
		{
			return 0.;
		}
	}

}
