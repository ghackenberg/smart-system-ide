package org.xtream.demo.hydro.model;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;

public class ContextComponent extends Component
{
	
	// Ports
	
	public Port<Double> hauptkraftwerkTurbineDischargeOutput = new Port<>();
	public Port<Double> wehr1TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr2TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr3TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr4TurbineDischargeOutput = new Port<>();
	
	public Port<Double> hauptkraftwerkWeirDischargeOutput = new Port<>();
	public Port<Double> wehr1WeirDischargeOutput = new Port<>();
	public Port<Double> wehr2WeirDischargeOutput = new Port<>();
	public Port<Double> wehr3WeirDischargeOutput = new Port<>();
	public Port<Double> wehr4WeirDischargeOutput = new Port<>();
	
	public Port<Double> speicherseeLevelOutput = new Port<>();
	public Port<Double> volumen1LevelOutput = new Port<>();
	public Port<Double> volumen2LevelOutput = new Port<>();
	public Port<Double> volumen3LevelOutput = new Port<>();
	public Port<Double> volumen4LevelOutput = new Port<>();
	public Port<Double> volumen5LevelOutput = new Port<>();
	
	public Port<Double> netProductionOutput = new Port<>();

}
