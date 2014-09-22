package org.xtream.core.model;

import java.net.URL;


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
