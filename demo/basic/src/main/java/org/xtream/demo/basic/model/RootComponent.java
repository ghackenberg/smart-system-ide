package org.xtream.demo.basic.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.builders.MapBuilder;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ChannelExpression;
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
	
	@Equivalence
	public Port<Double> firstOutput = new Port<>();
	
	@Equivalence
	public Port<Double> secondOutput = new Port<>();
	
	@Constraint
	public Port<Boolean> maximumOutput = new Port<>();
	
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
	
	public Expression<Double> channel1 = new ChannelExpression<>(add.firstInput, random1.output);
	
	public Expression<Double> channel2 = new ChannelExpression<>(add.secondInput, random2.output);
	
	public Expression<Double> channel3 = new ChannelExpression<>(integrate.input, random3.output);
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> test1Expression = new ConstantNonDeterministicExpression<>(firstOutput, new SetBuilder<Double>().add(1.).add(2.).add(3.).add(4.).add(5.).add(6.).add(7.).add(8.));
	
	public Expression<Double> test2Expression = new ConstantProbabilisticExpression<>(secondOutput, new MapBuilder<Double>().put(1., 0.5).put(2., 0.25).put(3.,  0.25));
	
	public Expression<Boolean> maximumExpression = new Expression<Boolean>(maximumOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return random1.output.get(timepoint) < 3.;
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	/* none */
}
