package org.xtream.core.model.components.nodes.shapes;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class CylinderComponent extends ShapeComponent
{

	// Ports
	
	public Port<Double> baseOutput = new Port<>();
	public Port<Double> heightOutput = new Port<>();

}
