package org.xtream.demo.projecthouse.model.room.window;

import org.xtream.core.model.containers.Module;

public class WindowModule extends Module{
	
	public double area;
	public double orientation;
	public BlindsController blindsController = new BlindsController();
	
	public WindowModule(double area, double orientation) {
		super();
		this.area = area;
		this.orientation = orientation;
	}

}
