package org.xtream.demo.hydro.model.split.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public class TurbineDischargeExpression extends Expression<Double>
{
	
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