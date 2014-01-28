package org.xtream.demo.thermal.model.stage;

import org.xtream.demo.thermal.model.SolarComponent;
import org.xtream.demo.thermal.model.Stage;
import org.xtream.demo.thermal.model.StorageComponent;
import org.xtream.demo.thermal.model.ThermalComponent;
import org.xtream.demo.thermal.model.smart.SmartSolarComponent;
import org.xtream.demo.thermal.model.smart.SmartStorageComponent;
import org.xtream.demo.thermal.model.smart.SmartThermalComponent;

public class StageC extends Stage
{

	@Override
	public SolarComponent createSolar(double scale)
	{
		return new SmartSolarComponent(scale);
	}

	@Override
	public StorageComponent createStorage(double speed, double capacity)
	{
		return new SmartStorageComponent(speed, capacity);
	}

	@Override
	public ThermalComponent createThermal()
	{
		return new SmartThermalComponent();
	}

}
