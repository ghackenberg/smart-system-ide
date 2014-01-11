package org.xtream.demo.basic.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.ports.InputPort;
import org.xtream.core.model.ports.OutputPort;

public class AddComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	@Show("Test")
	public InputPort<Double> input1 = new InputPort<>();

	@Show("Test")
	public InputPort<Double> input2 = new InputPort<>();
	
	/////////////
	// OUTPUTS //
	/////////////

	@Show("Test2")
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
		@Override public Double evaluate(int timepoint)
		{
			return input1.get(timepoint) + input2.get(timepoint);
		}
	};
	
}
