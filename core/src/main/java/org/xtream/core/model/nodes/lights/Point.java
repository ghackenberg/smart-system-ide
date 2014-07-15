package org.xtream.core.model.nodes.lights;

import org.xtream.core.data.Vector;
import org.xtream.core.model.State;
import org.xtream.core.model.nodes.Light;

public abstract class Point extends Light
{
	
	public abstract Vector getPosition(State state, int timepoint);

}
