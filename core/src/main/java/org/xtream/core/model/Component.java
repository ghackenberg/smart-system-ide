package org.xtream.core.model;

import java.net.URL;


public abstract class Component extends Element
{
	
	public Component()
	{
		this(Component.class.getClassLoader().getResource("component.png"));
	}
	public Component(URL icon)
	{
		super(icon);
	}

}
