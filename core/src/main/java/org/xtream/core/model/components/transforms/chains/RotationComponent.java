package org.xtream.core.model.components.transforms.chains;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.transforms.ChainComponent;

public abstract class RotationComponent extends ChainComponent
{
	
	// Ports
	
	public Port<Double> angleOutput = new Port<>();

}
