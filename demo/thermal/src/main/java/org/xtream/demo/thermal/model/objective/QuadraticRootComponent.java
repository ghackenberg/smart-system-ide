package org.xtream.demo.thermal.model.objective;

import org.xtream.core.model.Expression;
import org.xtream.demo.thermal.model.RootComponent;
import org.xtream.demo.thermal.model.Stage;

public abstract class QuadraticRootComponent extends RootComponent
{
	
	public QuadraticRootComponent(Stage stage)
	{
		super(stage);
	}
	
	// Expressions

	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += net.balanceOutput.get(timepoint) * net.balanceOutput.get(timepoint);
		}
	};

}
