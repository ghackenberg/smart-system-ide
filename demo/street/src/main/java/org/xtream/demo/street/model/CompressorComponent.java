package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class CompressorComponent extends Component
{
	
	// Ports
	
	public Port<Double> loadOutput = new Port<>();
	public Port<Double> heatOutput = new Port<>();
	
	// Expression
	
	public Expression<Double> loadExpression = new Expression<Double>(loadOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
	};
	public Expression<Double> heatExpression = new Expression<Double>(heatOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
	};

}
