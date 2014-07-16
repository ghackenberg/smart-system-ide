package org.xtream.core.model.containers;

import java.net.URL;

import org.xtream.core.model.Container;


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
