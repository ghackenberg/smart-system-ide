package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Show;

public abstract class EnergyComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	@Show("Main")
	public OutputPort<Double> production = new OutputPort<>();
	
	@Show("Main")
	public OutputPort<Double> consumption = new OutputPort<>();
	
	@Show("Main")
	public OutputPort<Double> balance = new OutputPort<>();
	
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

}
