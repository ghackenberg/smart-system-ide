package org.xtream.core.model.markers;

import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;

public abstract class Objective extends Marker<Double>
{
	
	public Objective(Port<Double> port)
	{
		super(port);
	}

}
