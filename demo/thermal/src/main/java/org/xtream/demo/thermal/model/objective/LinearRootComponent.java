package org.xtream.demo.thermal.model.objective;

import org.xtream.core.model.Expression;
import org.xtream.demo.thermal.model.RootComponent;
import org.xtream.demo.thermal.model.Stage;

public abstract class LinearRootComponent extends RootComponent
{
	
	public LinearRootComponent(Stage stage)
	{
		super(stage);
	}
	
	// Expressions

	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += Math.abs(net.balanceOutput.get(timepoint));
		}
	};

}
