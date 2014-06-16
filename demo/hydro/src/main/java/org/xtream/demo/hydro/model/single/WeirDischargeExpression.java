package org.xtream.demo.hydro.model.single;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public class WeirDischargeExpression extends Expression<Double>
{
	
	protected Port<Double> discharge;
	protected Port<Double> price;
	
	protected double turbineDischargeMax;
	protected double weirDischargeMax;

	public WeirDischargeExpression(Port<Double> port, Port<Double> discharge, Port<Double> price, double turbineDischargeMax, double weirDischargeMax)
	{
		super(port);
		
		this.discharge = discharge;
		this.price = price;
		
		this.turbineDischargeMax = turbineDischargeMax;
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (price.get(timepoint) >= 0)
		{
			return Math.max(discharge.get(timepoint) - turbineDischargeMax, 0);
		}
		else
		{
			return Math.min(discharge.get(timepoint), weirDischargeMax);
		}
	}

}
