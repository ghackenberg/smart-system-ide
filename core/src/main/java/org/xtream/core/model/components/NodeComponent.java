package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public abstract class NodeComponent extends Component
{
	
	public NodeComponent()
	{
		super(NodeComponent.class.getClassLoader().getResource("elements/node.png"));
	}

}
