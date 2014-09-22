package org.xtream.core.model.components;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class NodeComponent extends Component
{
	
	public NodeComponent()
	{
		super(NodeComponent.class.getClassLoader().getResource("elements/node.png"));
	}
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();

}
