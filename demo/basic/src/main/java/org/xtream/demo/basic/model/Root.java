package org.xtream.demo.basic.model;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Channel;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.expressions.SimpleRandomExpression;
import org.xtream.core.optimizer.Engine;
import org.xtream.demo.basic.model.system.Integrate;
import org.xtream.demo.basic.model.system.Random;

public class Root extends Component
{
	
	public static void main(String[] args)
	{
		new Engine(Root.class).run(96, 100, 0.5);
	}
	
	///////////
	// PORTS //
	///////////
	
	@Equivalence
	public OutputPort<Double> test = new OutputPort<>();
	
	@Constraint
	public OutputPort<Boolean> maximum = new OutputPort<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public Random random = new Random();
	
	public Random random1 = new Random();
	
	public Random random2 = new Random();
	
	public Integrate integrate = new Integrate();
	
	//////////////
	// CHANNELS //
	//////////////
	
	public Channel<Double> channel = new Channel<Double>(random.output, integrate.input);
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> testExpression = new SimpleRandomExpression<Double>(test)
	{
		@Override protected Set<Double> evaluateSet(int timepoint)
		{
			Set<Double> set = new HashSet<>();
			set.add(1.0);
			set.add(2.0);
			set.add(3.0);
			set.add(4.0);
			set.add(5.0);
			set.add(6.0);
			set.add(7.0);
			set.add(8.0);
			return set;
		}
	};
	
	public Expression<Boolean> maximumExpression = new Expression<Boolean>(maximum)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return random.output.get(timepoint) < 3.;
		}
	};
}
