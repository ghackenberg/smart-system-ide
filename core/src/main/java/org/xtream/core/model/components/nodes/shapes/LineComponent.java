package org.xtream.core.model.components.nodes.shapes;

import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class LineComponent extends ShapeComponent
{
	
	// Ports
	
	public Port<RealVector> startOutput = new Port<>();
	public Port<RealVector> endOutput = new Port<>();

}
