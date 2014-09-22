package org.xtream.core.model.components.nodes;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.NodeComponent;

public abstract class CameraComponent extends NodeComponent
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	
	public Port<RealVector> eyeOutput = new Port<>();
	public Port<RealVector> centerOutput = new Port<>();
	public Port<RealVector> upOutput = new Port<>();

}
