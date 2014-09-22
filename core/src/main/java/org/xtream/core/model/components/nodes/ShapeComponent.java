package org.xtream.core.model.components.nodes;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.NodeComponent;

public abstract class ShapeComponent extends NodeComponent
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	public Port<Color> colorOutput = new Port<>();

}
