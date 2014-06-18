package org.xtream.demo.hydro.model.single.continuous.forward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.optimizer.State;

public class DischargeExpression extends Expression<Double>
{
	
	protected double dischargeMax;

	@Reference
	protected Port<Double> previousLevel;
	protected double previousArea;
	protected double previousLevelMin;
	protected double previousLevelMax;

	@Reference
	protected Port<Double> previousDischarge;

	public DischargeExpression(Port<Double> weirDischarge, double dischargeMax, Port<Double> previousLevel, double previousArea, double previousLevelMax, Port<Double> previousDischarge)
	{
		super(weirDischarge, true);
		
		this.dischargeMax = dischargeMax;

		this.previousLevel = previousLevel;
		this.previousArea = previousArea;
		this.previousLevelMin = 0;
		this.previousLevelMax = previousLevelMax;
		
		this.previousDischarge = previousDischarge;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		if (timepoint > 0)
		{
			double minOption = (previousLevel.get(state, timepoint - 1) - previousLevelMin) * previousArea / 900 + previousDischarge.get(state, timepoint);
			double maxOption = (previousLevel.get(state, timepoint - 1) - previousLevelMax) * previousArea / 900 + previousDischarge.get(state, timepoint);
			
			minOption = Math.max(minOption, 0);
			maxOption = Math.min(maxOption, dischargeMax);
			
			if (maxOption >= minOption)
			{
				return minOption + (maxOption - minOption) * Math.random();
			}
			else
			{	
				return 0.;
			}
		}
		else
		{
			return 0.;
		}
	}

}
