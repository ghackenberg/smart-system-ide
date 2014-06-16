package org.xtream.demo.hydro.model.single.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

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
		super(discharge);
		
		this.dischargeMax = dischargeMax;

		this.nextLevel = nextLevel;
		this.nextArea = nextArea;
		this.nextLevelMin = 0;
		this.nextLevelMax = nextLevelMax;
		
		this.nextDischarge = nextDischarge;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (timepoint > 0)
		{
			double minOption = (nextLevelMin - nextLevel.get(timepoint - 1)) * nextArea / 900 + nextDischarge.get(timepoint);
			double maxOption = (nextLevelMax - nextLevel.get(timepoint - 1)) * nextArea / 900 + nextDischarge.get(timepoint);
			
			minOption = Math.max(minOption, 0);
			maxOption = Math.min(maxOption, dischargeMax);
			
			if (maxOption >= minOption)
			{
				return minOption + (maxOption - minOption) * Math.random();
			}
			else
			{
				System.out.println(getPort().getQualifiedName() + " (No option!)");
				
				return 0.;
			}
		}
		else
		{
			return 0.;
		}
	}

}
