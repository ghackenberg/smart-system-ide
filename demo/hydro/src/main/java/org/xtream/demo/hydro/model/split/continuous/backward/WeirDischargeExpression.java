package org.xtream.demo.hydro.model.split.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.optimizer.State;

public class WeirDischargeExpression extends Expression<Double>
{
	
	protected double weirDischargeMax;

	@Reference
	protected Port<Double> nextLevel;
	protected double nextArea;
	protected double nextLevelMin;
	protected double nextLevelMax;

	@Reference
	protected Port<Double> nextTurbineDischarge;
	@Reference
	protected Port<Double> nextWeirDischarge;
	
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
	public Double evaluate(State state, int timepoint)
	{
		if (timepoint > 0)
		{
			double outflow = nextTurbineDischarge.get(state, timepoint) + nextWeirDischarge.get(state, timepoint);
			double inflow = currentTurbineDischarge.get(state, timepoint);
			
			double minOption = nextLevelMin + outflow * 900 / nextArea - nextLevel.get(state, timepoint - 1) - inflow * 900 / nextArea;
			double maxOption = nextLevelMax + outflow * 900 / nextArea - nextLevel.get(state, timepoint - 1) - inflow * 900 / nextArea;
			
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
