package org.xtream.demo.hydro.model.control.single.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.State;

public class DischargeExpression extends Expression<Double>
{
	
	protected double dischargeMax;

	@Reference
	protected Port<Double> nextLevel;
	protected double nextArea;
	protected double nextLevelMin;
	protected double nextLevelMax;

	@Reference
	protected Port<Double> nextDischarge;

	public DischargeExpression(Port<Double> discharge, double dischargeMax, Port<Double> nextLevel, double nextArea, double nextLevelMax, Port<Double> nextDischarge)
	{
		super(discharge, true);
		
		this.dischargeMax = dischargeMax;

		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextDischarge = nextDischarge;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		if (timepoint > 0)
		{
			double minOption = (nextLevelMin - nextLevel.get(state, timepoint - 1)) * nextArea / 900 + nextDischarge.get(state, timepoint);
			double maxOption = (nextLevelMax - nextLevel.get(state, timepoint - 1)) * nextArea / 900 + nextDischarge.get(state, timepoint);
			
			minOption = Math.max(minOption, 0);
			maxOption = Math.min(maxOption, dischargeMax);
			
			if (maxOption >= minOption)
			{
				return minOption + (maxOption - minOption) * Math.random();
			}
			else
			{
				//System.out.println(getPort().getQualifiedName() + " (No option!)");
				
				return 0.;
			}
		}
		else
		{
			return 0.;
		}
	}

}
