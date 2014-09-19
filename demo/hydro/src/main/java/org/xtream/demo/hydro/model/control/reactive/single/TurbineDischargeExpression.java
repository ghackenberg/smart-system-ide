package org.xtream.demo.hydro.model.control.reactive.single;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.State;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	@Reference
	protected Port<Double> discharge;
	@Reference
	protected Port<Double> price;
	
	protected double turbineDischargeMax;
	protected double weirDischargeMax;

	public TurbineDischargeExpression(Port<Double> port, Port<Double> discharge, Port<Double> price, double turbineDischargeMax, double weirDischargeMax)
	{
		super(port);
		
		this.discharge = discharge;
		this.price = price;
		
		this.turbineDischargeMax = turbineDischargeMax;
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		if (price.get(state, timepoint) >= 0)
		{
			return Math.min(discharge.get(state, timepoint), turbineDischargeMax);
		}
		else
		{
			return Math.max(discharge.get(state, timepoint) - weirDischargeMax, 0);
		}
	}

}
