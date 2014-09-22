package org.xtream.demo.projecthouse.model.room.window;

import org.xtream.core.model.Module;

public class WindowModule extends Module{
	
	private double area;
	private double orientation;
	public BlindsController blindsController = new BlindsController();
	
	public WindowModule(WindowSpecification windowSpec) {
		super();
		area = windowSpec.area;
		orientation = windowSpec.orientation;
	}

	public double getArea() {
		return area;
	}

	public double getOrientation() {
		return orientation;
	}

}
