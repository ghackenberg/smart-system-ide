package org.xtream.core.model.components.nodes;

import java.awt.Color;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.NodeComponent;

public abstract class LightComponent extends NodeComponent
{
	
	// Ports
	
	public Port<Color> specularOutput = new Port<>();
	public Port<Color> diffuseOutput = new Port<>();

}
