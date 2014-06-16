package org.xtream.core.model.nodes;

import java.awt.Color;

import org.xtream.core.model.Node;

public abstract class Light extends Node
{
	
	public abstract Color getSpecular(int timepoint);
	public abstract Color getDiffuse(int timepoint);

}
