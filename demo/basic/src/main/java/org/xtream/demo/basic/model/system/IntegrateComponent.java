package org.xtream.demo.basic.model.system;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.InputPort;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Chart;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;

public class IntegrateComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public InputPort<Double> input = new InputPort<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	@Chart("Main")
	@Objective(Direction.MIN)
	public OutputPort<Double> output = new OutputPort<>();
	
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
	
}
