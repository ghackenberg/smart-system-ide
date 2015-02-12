package org.xtream.demo.ppu.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class ValveComponent extends Component
{
	
	// Ports
	
	public Port<Double> signalInput = new Port<>();
	public Port<Double> energyInput = new Port<>();
	
	public Port<Double> energyOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> energyExpression = new Expression<Double>(energyOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return signalInput.get(state, timepoint) == 0.0 ? 0.0 : energyInput.get(state, timepoint);
		}
	};

}
