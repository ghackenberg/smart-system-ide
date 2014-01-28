package org.xtream.demo.thermal.model;

import java.net.URL;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public abstract class EnergyComponent extends Component
{
	
	public EnergyComponent(URL icon)
	{
		super(icon);
	}
	
	// Ports
	
	public Port<Double> productionOutput = new Port<>();
	public Port<Double> consumptionOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return productionOutput.get(timepoint) + consumptionOutput.get(timepoint);
		}
	};

}
