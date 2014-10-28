package org.xtream.core.model.components;

import java.awt.Color;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class PlaneComponent extends Component
{
	// Ports
	
		public Port<Color> colorOutput = new Port<>();
		public Port<Double> heightOutput = new Port<>();
	
}
