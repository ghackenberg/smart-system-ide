package org.xtream.demo.hydro.model.discrete.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.discrete.ControlComponent
{
	
	// Ports
	
	public Port<Double> volumen5LevelInput = new Port<>(); // Dummy port
	public Port<Double> wehr5TurbineDischargeOutput = new Port<>(); // Dummy port
	public Port<Double> wehr5WeirDischargeOutput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> volumen5LevelExpression = new ConstantExpression<>(volumen5LevelInput, 0.);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkTurbineDischargeOptions, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, wehr1TurbineDischargeOptions, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, wehr2TurbineDischargeOptions, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, wehr3TurbineDischargeOptions, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, wehr4TurbineDischargeOptions, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput);
	public Expression<Double> wehr5TurbineDischargeExpression = new ConstantExpression<>(wehr5TurbineDischargeOutput, 0.);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, hauptkraftwerkWeirDischargeOptions, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, wehr1WeirDischargeOptions, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, wehr2WeirDischargeOptions, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, wehr3WeirDischargeOptions, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput, wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, wehr4WeirDischargeOptions, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput, wehr4TurbineDischargeOutput);
	public Expression<Double> wehr5WeirDischargeExpression = new ConstantExpression<>(wehr5WeirDischargeOutput, 0.);

}
