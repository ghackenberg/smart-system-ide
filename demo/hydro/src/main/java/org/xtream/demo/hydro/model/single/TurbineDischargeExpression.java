package org.xtream.demo.hydro.model.single;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	@Reference
	protected Port<Double> dischrage;
	@Reference
	protected Port<Double> price;
	
	protected double turbineDischargeMax;
	protected double weirDischargeMax;

	public TurbineDischargeExpression(Port<Double> port, Port<Double> discharge, Port<Double> price, double turbineDischargeMax, double weirDischargeMax)
	{
		super(port);
		
		this.dischrage = discharge;
		this.price = price;
		
		this.turbineDischargeMax = turbineDischargeMax;
		this.weirDischargeMax = weirDischargeMax;
	}

	@Override
	public Double evaluate(int timepoint)
	{
		if (price.get(timepoint) >= 0)
		{
			return Math.min(dischrage.get(timepoint), turbineDischargeMax);
		}
		else
		{
			return Math.max(dischrage.get(timepoint) - weirDischargeMax, 0);
		}
	}

}
