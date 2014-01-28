package org.xtream.demo.thermal.model;


public abstract class Stage
{
	
	public abstract SolarComponent createSolar(double scale);
	public abstract StorageComponent createStorage(double speed, double capacity);
	public abstract ThermalComponent createThermal();

}
