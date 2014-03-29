package org.xtream.demo.thermal.model.stage;

import org.xtream.demo.thermal.model.SolarComponent;
import org.xtream.demo.thermal.model.StorageComponent;
import org.xtream.demo.thermal.model.ThermalComponent;
import org.xtream.demo.thermal.model.smart.SmartStorageComponent;
import org.xtream.demo.thermal.model.standard.StandardSolarComponent;
import org.xtream.demo.thermal.model.standard.StandardThermalComponent;

public class StageA extends Stage
{

	@Override
	public SolarComponent createSolar(double scale)
	{
		return new StandardSolarComponent(scale);
	}

	@Override
	public StorageComponent createStorage(double speed, double capacity)
	{
		return new SmartStorageComponent(speed, capacity);
	}

	@Override
	public ThermalComponent createThermal()
	{
		return new StandardThermalComponent();
	}

}
