package org.xtream.core.model.containers;

import java.net.URL;

import org.xtream.core.model.Container;


public abstract class Module extends Container
{
	
	public Module()
	{
		this(Module.class.getClassLoader().getResource("elements/module.png"));
	}
	public Module(URL icon)
	{
		super(icon);
	}

}
