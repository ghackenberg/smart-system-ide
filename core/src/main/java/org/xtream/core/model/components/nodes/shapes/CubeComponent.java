package org.xtream.core.model.components.nodes.shapes;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class CubeComponent extends ShapeComponent
{
	
	// Ports
	
	public Port<Double> sizeOutput = new Port<>();

}
