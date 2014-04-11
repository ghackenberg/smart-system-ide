package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class ControlComponent extends Component
{
	
	// Ports
	
	public Port<Double> scenarioInflowInput = new Port<>();
	public Port<Double> scenarioPriceInput = new Port<>();
	
	public Port<Double> speicherseeLevelInput = new Port<>();
	public Port<Double> volumen1LevelInput = new Port<>();
	public Port<Double> volumen2LevelInput = new Port<>();
	public Port<Double> volumen3LevelInput = new Port<>();
	public Port<Double> volumen4LevelInput = new Port<>();
	
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

}
