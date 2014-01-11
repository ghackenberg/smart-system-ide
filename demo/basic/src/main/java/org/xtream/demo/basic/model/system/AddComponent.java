package org.xtream.demo.basic.model.system;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.InputPort;
import org.xtream.core.model.OutputPort;

public class AddComponent extends Component
{
	
	///////////
	// PORTS //
	///////////
	
	public InputPort<Double> input1 = new InputPort<>();
	
	public InputPort<Double> input2 = new InputPort<>();
	
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
