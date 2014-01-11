package org.xtream.demo.basic.model;

import org.xtream.core.model.Channel;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.builders.MapBuilder;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.expressions.ConstantProbabilisticExpression;
import org.xtream.core.optimizer.Engine;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Engine<>(RootComponent.class).run(96, 1000, 0);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	@Show({"Test", "Test1"})
	@Equivalence
	public OutputPort<Double> test1 = new OutputPort<>();
	
	@Show({"Test", "Test2"})
	@Equivalence
	public OutputPort<Double> test2 = new OutputPort<>();
	
	@Constraint
	public OutputPort<Boolean> maximum = new OutputPort<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public RandomComponent random1 = new RandomComponent();
	
	public RandomComponent random2 = new RandomComponent();
	
	public RandomComponent random3 = new RandomComponent();
	
	public AddComponent add = new AddComponent();
	
	public IntegrateComponent integrate = new IntegrateComponent();
	
	//////////////
	// CHANNELS //
	//////////////
	
	public Channel<Double> channel1 = new Channel<Double>(random1.output, add.input1);
	
	public Channel<Double> channel2 = new Channel<Double>(random2.output, add.input2);
	
	public Channel<Double> channel3 = new Channel<Double>(random3.output, integrate.input);
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> test1Expression = new ConstantNonDeterministicExpression<>(test1, new SetBuilder<Double>().add(1.).add(2.).add(3.).add(4.).add(5.).add(6.).add(7.).add(8.));
	
	public Expression<Double> test2Expression = new ConstantProbabilisticExpression<>(test2, new MapBuilder<Double>().put(1., 0.5).put(2., 0.25).put(3.,  0.25));
	
	public Expression<Boolean> maximumExpression = new Expression<Boolean>(maximum)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return random1.output.get(timepoint) < 3.;
		}
	};
}
