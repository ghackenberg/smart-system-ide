package org.xtream.demo.hydro.model.continuous.forward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.continuous.ControlComponent
{
	
	// Ports
	
	public Port<Double> dummyInflowInput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> dummyInflowExpression = new ConstantExpression<Double>(dummyInflowInput, 0.);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, scenarioInflowInput, dummyInflowInput);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, Constants.WEHR1_TURBINE_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, Constants.WEHR2_TURBINE_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, Constants.WEHR3_TURBINE_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, Constants.WEHR4_TURBINE_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, scenarioInflowInput, dummyInflowInput, hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, Constants.WEHR1_WEIR_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkWeirDischargeOutput, wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, Constants.WEHR2_WEIR_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, Constants.WEHR3_WEIR_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, Constants.WEHR4_WEIR_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr4TurbineDischargeOutput);

}
