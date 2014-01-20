package org.xtream.core.model.components;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class AggregatorComponent extends Component
{
	
	@SuppressWarnings("unchecked")
	public AggregatorComponent(int size)
	{
		itemInputs = new Port[size];
		for (int i = 0; i < size; i++)
		{
			itemInputs[i] = new Port<>();
		}
	}
	
	// Inputs
	
	public Port<Double>[] itemInputs;
	
	// Outputs
	
	public Port<Double> valueOutput = new Port<>();
	
	// Charts
	
	public Chart valueChart = new Chart(valueOutput);

}
