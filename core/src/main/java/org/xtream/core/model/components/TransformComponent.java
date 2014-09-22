package org.xtream.core.model.components;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class TransformComponent extends Component
{
	
	public TransformComponent()
	{
		super(TransformComponent.class.getClassLoader().getResource("elements/transform.png"));
	}
	
	// Ports
	
	public Port<RealMatrix> transformOutput = new Port<RealMatrix>();

}
