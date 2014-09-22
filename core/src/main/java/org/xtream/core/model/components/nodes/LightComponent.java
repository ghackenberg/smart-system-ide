package org.xtream.core.model.components.nodes;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.NodeComponent;

public abstract class LightComponent extends NodeComponent
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	public Port<Color> specularOutput = new Port<>();
	public Port<Color> diffuseOutput = new Port<>();

}
