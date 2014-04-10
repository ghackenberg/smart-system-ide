package org.xtream.demo.hydro.model.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	@Constant
	protected double turbineDischargeMax;
	
	public TurbineDischargeExpression(Port<Double> port, double turbineDischargeMax)
	{
		super(port);
		
		this.turbineDischargeMax = turbineDischargeMax;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		return Math.random() * turbineDischargeMax;
	}

}
