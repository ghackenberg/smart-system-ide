package org.xtream.demo.basic.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;

public class IntegrateComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> input = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	@Objective(Direction.MIN)
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
		public double previous = 0;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += input.get(timepoint);
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	public Chart outputChart = new Chart(output); 
	
}
