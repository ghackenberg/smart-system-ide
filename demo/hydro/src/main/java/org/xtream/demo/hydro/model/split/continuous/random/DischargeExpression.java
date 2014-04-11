package org.xtream.demo.hydro.model.split.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class DischargeExpression extends Expression<Double>
{
	
	@Constant
	protected double weirDischargeMax;
	
	public DischargeExpression(Port<Double> port, double weirDischargeMax)
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
