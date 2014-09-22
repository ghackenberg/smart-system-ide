package org.xtream.core.model.components.transforms;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class ChainComponent extends Component
{
	
	// Ports

	public Port<RealMatrix> transformInput = new Port<RealMatrix>();
	
	public Port<RealMatrix> transformOutput = new Port<RealMatrix>();

}
