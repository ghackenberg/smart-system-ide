package org.xtream.demo.basic.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class AddComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> firstInput = new Port<>();

	public Port<Double> secondInput = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////

	public Port<Double> output = new Port<>();
	
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

	public Expression<Double> outputExpression = new Expression<Double>(output)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return firstInput.get(state, timepoint) + secondInput.get(state, timepoint);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	/* none */
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	/* none */
	
	////////////
	// CHARTS //
	////////////
	
	/* none */
	
}
