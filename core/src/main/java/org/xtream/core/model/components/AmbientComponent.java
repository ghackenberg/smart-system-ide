package org.xtream.core.model.components;

import java.awt.Color;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class AmbientComponent extends Component
{
	
	public AmbientComponent()
	{
		super(AmbientComponent.class.getClassLoader().getResource("elements/node.png"));
	}
	
	// Ports
	
	public Port<Color> colorOutput = new Port<>();
	
}
