package org.xtream.demo.ppu.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class CompressorComponent extends Component
{
	
	// Constructors
	
	public CompressorComponent(double energy)
	{
		this.energy = energy;
	}
	
	// Parameters
	
	private double energy;
	
	// Ports
	
	public Port<Double> energyOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> energyExpression = new Expression<Double>(energyOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return energy;
		}
	};

}
