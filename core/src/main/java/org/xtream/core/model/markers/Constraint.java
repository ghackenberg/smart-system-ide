package org.xtream.core.model.markers;

import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;

public class Constraint extends Marker<Boolean>
{

	public Constraint(Port<Boolean> port)
	{
		super(port);
	}

}
