package org.xtream.core.model.components.nodes;

import java.awt.Color;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.NodeComponent;

public abstract class BackgroundComponent extends NodeComponent
{
	
	// Ports
	
	public Port<Color> colorOutput = new Port<>();

}
