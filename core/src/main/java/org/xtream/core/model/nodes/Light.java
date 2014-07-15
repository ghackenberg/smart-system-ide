package org.xtream.core.model.nodes;

import java.awt.Color;

import org.xtream.core.model.Node;
import org.xtream.core.model.State;

public abstract class Light extends Node
{
	
	public abstract Color getSpecular(State state, int timepoint);
	public abstract Color getDiffuse(State state, int timepoint);

}
