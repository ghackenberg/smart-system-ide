package org.xtream.demo.basic.model.system;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.expressions.NonDeterministicExpression;

public class Random extends Component
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
	
	public Expression<Double> outputExpression = new NonDeterministicExpression<Double>(output)
	{
		@Override protected Set<Double> evaluateSet(int timepoint)
		{
			Set<Double> set = new HashSet<>();
			set.add(1.);
			set.add(2.);
			set.add(3.);
			return set;
		}
	};
	
}
