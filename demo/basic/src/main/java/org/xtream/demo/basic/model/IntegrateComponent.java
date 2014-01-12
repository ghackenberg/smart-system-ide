package org.xtream.demo.basic.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.model.ports.InputPort;
import org.xtream.core.model.ports.OutputPort;

public class IntegrateComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public InputPort<Double> input = new InputPort<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
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
	
	////////////
	// CHARTS //
	////////////
	
	public Chart outputChart = new Chart(output); 
	
}
