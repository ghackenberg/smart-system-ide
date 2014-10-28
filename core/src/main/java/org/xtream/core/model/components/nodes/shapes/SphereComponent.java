package org.xtream.core.model.components.nodes.shapes;

import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class SphereComponent extends ShapeComponent
{
	
	// Ports
	
	public Port<RealVector> positionOutput= new Port<>();
	public Port<Double> radiusOutput = new Port<>();

}
