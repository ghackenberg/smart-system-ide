package org.xtream.demo.hydro.model.control.split.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class WeirDischargeExpression extends Expression<Double>
{
	
	protected double weirDischargeMax;
	
	public WeirDischargeExpression(Port<Double> port, double weirDischargeMax)
	{
		super(port, true);
		
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	protected Double evaluate(State state, int timepoint)
	{
		return Math.random() * weirDischargeMax;
	}

}
