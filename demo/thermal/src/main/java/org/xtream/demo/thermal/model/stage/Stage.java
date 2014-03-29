package org.xtream.demo.thermal.model.stage;

import org.xtream.demo.thermal.model.SolarComponent;
import org.xtream.demo.thermal.model.StorageComponent;
import org.xtream.demo.thermal.model.ThermalComponent;


public abstract class Stage
{
	
	public abstract SolarComponent createSolar(double scale);
	public abstract StorageComponent createStorage(double speed, double capacity);
	public abstract ThermalComponent createThermal();

}
