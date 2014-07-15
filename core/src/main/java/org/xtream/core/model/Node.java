package org.xtream.core.model;

public abstract class Node extends Element
{
	
	public Node()
	{
		super(Node.class.getClassLoader().getResource("elements/node.png"));
	}

}
