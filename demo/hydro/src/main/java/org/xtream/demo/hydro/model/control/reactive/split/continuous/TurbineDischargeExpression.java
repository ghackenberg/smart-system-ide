package org.xtream.demo.hydro.model.control.reactive.split.continuous;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	protected double turbineDischargeMax;
	
	public TurbineDischargeExpression(Port<Double> port, double turbineDischargeMax)
	{
		super(port, true);
		
		this.turbineDischargeMax = turbineDischargeMax;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		return Math.random() * turbineDischargeMax;
	}

}