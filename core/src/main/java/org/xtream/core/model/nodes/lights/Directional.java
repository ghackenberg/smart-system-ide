package org.xtream.core.model.nodes.lights;

import org.xtream.core.model.Vector;
import org.xtream.core.model.nodes.Light;

public abstract class Directional extends Light
{
	
	public abstract Vector getDirection(int timepoint);

}
