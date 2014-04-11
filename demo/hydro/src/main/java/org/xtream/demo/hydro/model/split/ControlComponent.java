package org.xtream.demo.hydro.model.split;

import org.xtream.core.model.Port;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Ports
	
	public Port<Double> hauptkraftwerkDischargeOutput = new Port<>();
	public Port<Double> wehr1DischargeOutput = new Port<>();
	public Port<Double> wehr2DischargeOutput = new Port<>();
	public Port<Double> wehr3DischargeOutput = new Port<>();
	public Port<Double> wehr4DischargeOutput = new Port<>();

}
