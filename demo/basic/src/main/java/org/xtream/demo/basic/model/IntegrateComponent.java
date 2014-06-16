package org.xtream.demo.basic.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.charts.Timeline;
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
		// NOTE Will be managed by the optimizer (like all public fields of expressions except with annotation @Constant)!
		public double previous = 0;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += input.get(timepoint);
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
	
	public Objective outputObjective = new Objective(output, Direction.MIN);
	
	////////////
	// CHARTS //
	////////////
	
	public Timeline outputChart = new Timeline(output); 
	
}
