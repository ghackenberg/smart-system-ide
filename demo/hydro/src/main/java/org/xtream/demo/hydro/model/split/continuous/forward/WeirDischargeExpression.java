package org.xtream.demo.hydro.model.split.continuous.forward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.State;

public class WeirDischargeExpression extends Expression<Double>
{
	
	protected double weirDischargeMax;

	@Reference
	protected Port<Double> previousLevel;
	protected double previousArea;
	protected double previousLevelMin;
	protected double previousLevelMax;

	@Reference
	protected Port<Double> previousTurbineDischarge;
	@Reference
	protected Port<Double> previousWeirDischarge;

	@Reference
	protected Port<Double> currentTurbineDischarge;

	public WeirDischargeExpression(Port<Double> weirDischarge, double weirDischargeMax, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousTurbineDischarge, Port<Double> previousWeirDischarge, Port<Double> currentTurbineDischrage)
	{
		super(weirDischarge, true);
		
		this.weirDischargeMax = weirDischargeMax;

		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousTurbineDischarge = previousTurbineDischarge;
		this.previousWeirDischarge = previousWeirDischarge;
		
		this.currentTurbineDischarge = currentTurbineDischrage;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		if (timepoint > 0)
		{
			double outflow = currentTurbineDischarge.get(state, timepoint);
			double inflow = previousTurbineDischarge.get(state, timepoint) + previousWeirDischarge.get(state, timepoint);
			
			double minOption = previousLevelMin + outflow * 900 / previousArea - previousLevel.get(state, timepoint - 1) - inflow * 900 / previousArea;
			double maxOption = previousLevelMax + outflow * 900 / previousArea - previousLevel.get(state, timepoint - 1) - inflow * 900 / previousArea;
			
			minOption = Math.max(minOption, 0.);
			maxOption = Math.min(maxOption, weirDischargeMax * 900 / previousArea);
			
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
