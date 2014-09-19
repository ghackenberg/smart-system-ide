package org.xtream.demo.hydro.model.control.reactive.single.continuous;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class DischargeLastExpression extends Expression<Double>
{
	
	protected double dischargeMin;
	protected double dischargeMax;
	
	public DischargeLastExpression(Port<Double> port, double dischargeMin, double dischargeMax)
	{
		super(port, true);
		
		this.dischargeMin = dischargeMin;
		this.dischargeMax = dischargeMax;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		if (timepoint == 0)
		{
			return dischargeMin + (dischargeMax - dischargeMin) * Math.random();
		}
		else
		{
			double last = getPort().get(state, timepoint - 1);
			
			return last * 0.94 + (last * 1.125 - last * 0.94) * Math.random();
		}
	}

}
