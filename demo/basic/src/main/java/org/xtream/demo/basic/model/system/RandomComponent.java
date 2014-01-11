package org.xtream.demo.basic.model.system;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class RandomComponent extends Component
{
	
	///////////
	// PORTS //
	///////////
	
	public OutputPort<Double> output = new OutputPort<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	/* none */
	
	//////////////
	// CHANNELS //
	//////////////
	
	/* none */
	
	////////////////
	// EXPRESSION //
	////////////////
	
	public Expression<Double> outputExpression = new ConstantNonDeterministicExpression<Double>(output, new SetBuilder<Double>().add(1.).add(2.).add(3.));
	
}
