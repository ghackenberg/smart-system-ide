package org.xtream.demo.hydro.model.split;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class TurbineDischargeExpression extends Expression<Double>
{
	
	@Constant
	protected Port<Double> dischrage;
	@Constant
	protected Port<Double> price;
	
	@Constant
	protected double turbineDischargeMax;
	@Constant
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
			return dischrage.get(timepoint) - weirDischargeMax;
		}
	}

}
