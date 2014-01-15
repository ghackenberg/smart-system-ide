package org.xtream.demo.thermal.model;

import org.xtream.core.model.Port;

public class ControlComponent
{
	
	@SuppressWarnings("unchecked")
	public ControlComponent(int size)
	{
		thermalOutputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			thermalOutputs[i] = new Port<>();
		}
	}
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> storageInput = new Port<>();
	public Port<Double> solarInput = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double>[] thermalOutputs;

}
