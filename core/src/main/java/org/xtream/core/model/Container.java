package org.xtream.core.model;

import java.net.URL;

public class Container extends Element
{
	
	public Container()
	{
		this(Container.class.getClassLoader().getResource("elements/container.png"));
	}
	public Container(URL icon)
	{
		super(icon);
	}

}
