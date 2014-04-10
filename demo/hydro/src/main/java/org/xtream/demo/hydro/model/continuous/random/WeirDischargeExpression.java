package org.xtream.demo.hydro.model.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class WeirDischargeExpression extends Expression<Double>
{
	
	@Constant
	protected double weirDischargeMax;
	
	public WeirDischargeExpression(Port<Double> port, double weirDischargeMax)
	{
		super(port);
		
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		return Math.random() * weirDischargeMax;
	}

}
