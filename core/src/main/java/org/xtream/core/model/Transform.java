package org.xtream.core.model;

public abstract class Transform extends Element
{
	
	public Transform()
	{
		super(Transform.class.getClassLoader().getResource("elements/transform.png"));
	}

}
