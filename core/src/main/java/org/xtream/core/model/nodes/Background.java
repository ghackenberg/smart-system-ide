package org.xtream.core.model.nodes;

import java.awt.Color;

import org.xtream.core.model.Node;

public abstract class Background extends Node
{
	
	public abstract Color getColor(int timepoint);

}
