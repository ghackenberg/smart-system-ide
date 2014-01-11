package org.xtream.demo.basic.model;

import org.xtream.core.model.Channel;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.builders.MapBuilder;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.expressions.ConstantProbabilisticExpression;
import org.xtream.core.optimizer.Engine;
import org.xtream.demo.basic.model.system.IntegrateComponent;
import org.xtream.demo.basic.model.system.RandomComponent;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Engine(RootComponent.class).run(96, 100, 0.5);
	}
	
	///////////
	// PORTS //
	///////////
	
	@Equivalence
	public OutputPort<Double> test1 = new OutputPort<>();
	
	@Equivalence
	public OutputPort<Double> test2 = new OutputPort<>();
	
	@Constraint
	public OutputPort<Boolean> maximum = new OutputPort<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public RandomComponent random = new RandomComponent();
	
	public RandomComponent random1 = new RandomComponent();
	
	public RandomComponent random2 = new RandomComponent();
	
	public IntegrateComponent integrate = new IntegrateComponent();
	
	//////////////
	// CHANNELS //
	//////////////
	
	public Channel<Double> channel = new Channel<Double>(random.output, integrate.input);
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> test1Expression = new ConstantNonDeterministicExpression<>(test1, new SetBuilder<Double>().add(1.).add(2.).add(3.));
	
	public Expression<Double> test2Expression = new ConstantProbabilisticExpression<>(test2, new MapBuilder<Double>().put(1., 0.5).put(2., 0.25).put(3.,  0.25));
	
	public Expression<Boolean> maximumExpression = new Expression<Boolean>(maximum)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return random.output.get(timepoint) < 3.;
		}
	};
}
