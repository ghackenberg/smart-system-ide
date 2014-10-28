package org.xtream.core.model.components.nodes.shapes;

import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class CubeComponent extends ShapeComponent 
{

	// Ports

	public Port<Double> sizeOutput = new Port<>();
	public Port<RealVector> positionOutput = new Port<>();
	public Port<RealVector> directionOutput = new Port<>();	


}
