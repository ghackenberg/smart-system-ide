package org.xtream.core.model.components;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;

public abstract class AbstractCostsComponent extends Component
{
	
	// Outputs
	
	public Port<Double> costsOutput = new Port<>();

}
