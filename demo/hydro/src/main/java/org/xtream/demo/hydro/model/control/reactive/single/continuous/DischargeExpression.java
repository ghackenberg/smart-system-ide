package org.xtream.demo.hydro.model.control.reactive.single.continuous;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class DischargeExpression extends Expression<Double>
{
	
	protected double dischargeMin;
	protected double dischargeMax;
	
	public DischargeExpression(Port<Double> port, double dischargeMin, double dischargeMax)
	{
		super(port, true);
		
		this.dischargeMin = dischargeMin;
		this.dischargeMax = dischargeMax;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		return dischargeMin + (dischargeMax - dischargeMin) * Math.random();
	}

}
