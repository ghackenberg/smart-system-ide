package org.xtream.core.model.components;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;

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

}
