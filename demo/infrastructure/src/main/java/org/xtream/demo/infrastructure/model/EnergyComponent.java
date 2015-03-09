package org.xtream.demo.infrastructure.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public abstract class EnergyComponent extends Component
{

	// Ports

	public Port<Double> costOutput = new Port<>();
	public Port<Boolean> validOutput = new Port<>();
	public Port<Double> powerOutput = new Port<>();
	public Port<Double> costAggregatedOutput = new Port<>();
	
	public Expression<Double> costAggregatedExpression = new Expression<Double>(costAggregatedOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = costOutput.get(state, timepoint);
			
			return ((timepoint == 0 ? 0 : costOutput.get(state, timepoint - 1)) + sum);
		}
	};
}
