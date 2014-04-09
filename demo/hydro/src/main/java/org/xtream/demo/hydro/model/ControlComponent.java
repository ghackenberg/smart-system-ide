package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantExpression;

public class ControlComponent extends Component
{
	
	// Parameters
	
	protected SetBuilder<Double> hauptkraftwerkTurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(7.5).add(15.).add(22.5).add(30.);
	protected SetBuilder<Double> wehr1TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(6.).add(12.).add(18.).add(24.);
	protected SetBuilder<Double> wehr2TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(6.).add(12.).add(18.).add(24.);
	protected SetBuilder<Double> wehr3TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(6.).add(12.).add(18.).add(24.);
	protected SetBuilder<Double> wehr4TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(1.8).add(3.6).add(5.4).add(7.2);
	
	protected SetBuilder<Double> hauptkraftwerkWeirDischargeOptions = new SetBuilder<Double>().add(0.).add(2.5).add(5.);
	protected SetBuilder<Double> wehr1WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(2.5).add(5.);
	protected SetBuilder<Double> wehr2WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(2.5).add(5.);
	protected SetBuilder<Double> wehr3WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(2.5).add(5.);
	protected SetBuilder<Double> wehr4WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(2.5).add(5.);
	
	// Ports
	
	public Port<Double> scenarioInflowInput = new Port<>();
	
	public Port<Double> speicherseeLevelInput = new Port<>();
	public Port<Double> volumen1LevelInput = new Port<>();
	public Port<Double> volumen2LevelInput = new Port<>();
	public Port<Double> volumen3LevelInput = new Port<>();
	public Port<Double> volumen4LevelInput = new Port<>();
	public Port<Double> volumen5LevelInput = new Port<>(); // Dummy port
	
	public Port<Double> hauptkraftwerkTurbineDischargeOutput = new Port<>();
	public Port<Double> wehr1TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr2TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr3TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr4TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr5TurbineDischargeOutput = new Port<>(); // Dummy port
	
	public Port<Double> hauptkraftwerkWeirDischargeOutput = new Port<>();
	public Port<Double> wehr1WeirDischargeOutput = new Port<>();
	public Port<Double> wehr2WeirDischargeOutput = new Port<>();
	public Port<Double> wehr3WeirDischargeOutput = new Port<>();
	public Port<Double> wehr4WeirDischargeOutput = new Port<>();
	public Port<Double> wehr5WeirDischargeOutput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> volumen5LevelExpression = new ConstantExpression<>(volumen5LevelInput, 0.);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, hauptkraftwerkTurbineDischargeOptions.set);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr1TurbineDischargeOptions.set);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr2TurbineDischargeOptions.set);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput, wehr3TurbineDischargeOptions.set);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput, wehr4TurbineDischargeOptions.set);
	public Expression<Double> wehr5TurbineDischargeExpression = new ConstantExpression<>(wehr5TurbineDischargeOutput, 0.);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkWeirDischargeOptions.set);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr1TurbineDischargeOutput, wehr1TurbineDischargeOptions.set);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr2TurbineDischargeOutput, wehr2TurbineDischargeOptions.set);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput, wehr3TurbineDischargeOutput, wehr3TurbineDischargeOptions.set);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput, wehr4TurbineDischargeOutput, wehr4TurbineDischargeOptions.set);
	public Expression<Double> wehr5WeirDischargeExpression = new ConstantExpression<>(wehr5WeirDischargeOutput, 0.);

}
