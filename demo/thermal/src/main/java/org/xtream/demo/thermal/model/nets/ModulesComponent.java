package org.xtream.demo.thermal.model.nets;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractModulesComponent;

public abstract class ModulesComponent extends AbstractModulesComponent
{
	
	@SuppressWarnings("unchecked")
	public ModulesComponent(int size)
	{
		// Balance outputs
		
		balanceOutputs = new Port[size + 2];
		
		for (int i = 0; i < size + 2; i++)
		{
			balanceOutputs[i] = new Port<>();
		}
	}
	
	// Ports
	
	public Port<Double>[] balanceOutputs;
	
}
