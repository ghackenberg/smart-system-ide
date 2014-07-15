package org.xtream.demo.thermal.model.objective;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.thermal.model.RootComponent;
import org.xtream.demo.thermal.model.stage.Stage;

public abstract class LinearRootComponent extends RootComponent
{
	
	public LinearRootComponent(Stage stage)
	{
		super(stage);
	}
	
	// Expressions

	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint > 0 ? costOutput.get(state, timepoint - 1) : 0) + Math.abs(net.balanceOutput.get(state, timepoint));
		}
	};

}
