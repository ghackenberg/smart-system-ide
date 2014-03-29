package org.xtream.demo.thermal.model.objective;

import org.xtream.core.model.Expression;
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

	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (timepoint > 0 ? costOutput.get(timepoint - 1) : 0) + net.balanceOutput.get(timepoint) / scale * net.balanceOutput.get(timepoint) / scale;
		}
	};

}
