package org.xtream.core.model;

import java.net.URL;


public abstract class Component extends Container
{
	
	public Component()
	{
		this(Component.class.getClassLoader().getResource("elements/component.png"));
	}
	public Component(URL icon)
	{
		super(icon);
	}

}
