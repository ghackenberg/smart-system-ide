package org.xtream.core.model.nodes;

import java.awt.Color;

import org.xtream.core.model.Node;
import org.xtream.core.model.State;

public abstract class Background extends Node
{
	
	public abstract Color getColor(State state, int timepoint);

}
