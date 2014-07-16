package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;

public class NetComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public NetComponent(int size)
	{
		loadInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			loadInputs[i] = new Port<Double>();
		}
	}
	
	// Ports

	public Port<Double>[] loadInputs;
	
	public Port<Double> balanceOutput = new Port<Double>();
	
	// Charts
	
	public Chart balanceChart = new Timeline(balanceOutput);
	
	// Expressions
	
	public Expression<Double> balanceExpr = new Expression<Double>(balanceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0.0;
			
			for (Port<Double> loadInput : loadInputs)
			{
				sum += loadInput.get(state, timepoint);
			}
			
			return sum;
		}
	};

}
