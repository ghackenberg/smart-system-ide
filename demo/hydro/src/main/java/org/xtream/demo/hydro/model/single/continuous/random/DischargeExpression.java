package org.xtream.demo.hydro.model.single.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class DischargeExpression extends Expression<Double>
{
	
	protected double weirDischargeMax;
	
	public DischargeExpression(Port<Double> port, double weirDischargeMax)
	{
		super(port);
		
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	public Double evaluate(State state, int timepoint)
	{
		return Math.random() * weirDischargeMax;
	}

}
