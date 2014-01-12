package org.xtream.demo.basic.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.ports.OutputPort;

public class RandomComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
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
	
	////////////
	// CHARTS //
	////////////
	
	/* none */
	
}
