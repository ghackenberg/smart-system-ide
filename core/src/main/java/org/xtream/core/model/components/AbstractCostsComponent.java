package org.xtream.core.model.components;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class AbstractCostsComponent extends Component
{
	
	// Outputs
	
	public Port<Double> costsOutput = new Port<>();

}
