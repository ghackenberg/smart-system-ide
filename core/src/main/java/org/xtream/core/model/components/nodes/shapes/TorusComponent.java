package org.xtream.core.model.components.nodes.shapes;

import org.xtream.core.model.Port;
import org.xtream.core.model.components.nodes.ShapeComponent;

public abstract class TorusComponent extends ShapeComponent
{
	// Ports
	
		public Port<Double> innerRadiusOutput = new Port<>();
		public Port<Double> outerRadiusOutput = new Port<>();

}
