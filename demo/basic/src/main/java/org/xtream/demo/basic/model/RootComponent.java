package org.xtream.demo.basic.model;

import org.xtream.core.data.Pair;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.expressions.ConstantProbabilisticExpression;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new RootComponent(), 96, 100, 10, 0, 0);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> firstOutput = new Port<>();
	
	public Port<Double> secondOutput = new Port<>();
	
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
	
	public Expression<Double> test1Expression = new ConstantNonDeterministicExpression<>(firstOutput, 1., 2., 3., 4., 5., 6., 7., 8.);
	
	public Expression<Double> test2Expression = new ConstantProbabilisticExpression<>(secondOutput, new Pair<>(1., 0.5), new Pair<>(2., 0.25), new Pair<>(3.,  0.25));
	
	public Expression<Boolean> maximumExpression = new Expression<Boolean>(maximumOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return random1.output.get(state, timepoint) < 3.;
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	public Constraint maximumConstraint = new Constraint(maximumOutput);
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	public Equivalence firstEquivalence = new Equivalence(firstOutput);
	
	public Equivalence secondEquivalence = new Equivalence(secondOutput);
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	/* none */
	
	////////////
	// CHARTS //
	////////////
	
	/* none */
}
