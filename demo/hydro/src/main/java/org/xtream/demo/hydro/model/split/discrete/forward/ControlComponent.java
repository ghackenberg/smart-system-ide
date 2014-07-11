package org.xtream.demo.hydro.model.split.discrete.forward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.split.discrete.ControlComponent
{
	
	// Ports
	
	public Port<Double> dummyInflowInput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> dummyInflowExpression = new ConstantExpression<Double>(dummyInflowInput, 0.);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkTurbineDischargeOptions, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, inflowInput, dummyInflowInput);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, wehr1TurbineDischargeOptions, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, wehr2TurbineDischargeOptions, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, wehr3TurbineDischargeOptions, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, wehr4TurbineDischargeOptions, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, hauptkraftwerkWeirDischargeOptions, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, inflowInput, dummyInflowInput, hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, wehr1WeirDischargeOptions, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkWeirDischargeOutput, wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, wehr2WeirDischargeOptions, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, wehr3WeirDischargeOptions, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, wehr4WeirDischargeOptions, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr4TurbineDischargeOutput);

}
