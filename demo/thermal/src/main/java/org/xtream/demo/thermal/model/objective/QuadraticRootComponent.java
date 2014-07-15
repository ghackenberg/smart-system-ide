package org.xtream.demo.thermal.model.objective;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.thermal.model.RootComponent;
import org.xtream.demo.thermal.model.stage.Stage;

public abstract class QuadraticRootComponent extends RootComponent
{
	
	public QuadraticRootComponent(Stage stage)
	{
		super(stage);
		
		scale = size * 600;
	}
	
	protected double scale;
	
	// Expressions

	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint > 0 ? costOutput.get(state, timepoint - 1) : 0) + net.balanceOutput.get(state, timepoint) / scale * net.balanceOutput.get(state, timepoint) / scale;
		}
	};

}
