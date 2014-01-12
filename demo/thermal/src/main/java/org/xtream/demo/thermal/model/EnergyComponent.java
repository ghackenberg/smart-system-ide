package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public abstract class EnergyComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> production = new Port<>();
	
	public Port<Double> consumption = new Port<>();
	
	public Port<Double> balance = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////

	/* none */
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> balanceExpression = new Expression<Double>(balance)
	{
		@Override public Double evaluate(int timepoint)
		{
			return production.get(timepoint) + consumption.get(timepoint);
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	/* none */

}
