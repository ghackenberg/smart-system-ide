package org.xtream.demo.infrastructure.model.scenario.factories;

import org.xtream.demo.infrastructure.model.power.SolarPanelComponent;

public class SolarPanelFactory {
	
	public SolarPanelComponent generateSolarPanel(Double powerScale) 
	{
		return new SolarPanelComponent(powerScale);
	}
	
	public SolarPanelComponent generateRandomSolarPanel(Double maximumPowerScale, Double maximumPowerScaleRandomness) 
	{
		return new SolarPanelComponent(maximumPowerScale-(Math.random()*maximumPowerScaleRandomness));
	}

}
