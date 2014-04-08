package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ControlComponent extends Component
{
	
	// Parameters
	
	protected SetBuilder<Double> hauptkraftwerkTurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(15.).add(30.);
	protected SetBuilder<Double> wehr1TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(12.).add(24.);
	protected SetBuilder<Double> wehr2TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(12.).add(24.);
	protected SetBuilder<Double> wehr3TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(12.).add(24.);
	protected SetBuilder<Double> wehr4TurbineDischargeOptions = new SetBuilder<Double>().add(0.).add(1.).add(7.2);
	
	protected SetBuilder<Double> hauptkraftwerkWeirDischargeOptions = new SetBuilder<Double>().add(0.);
	protected SetBuilder<Double> wehr1WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(5.);
	protected SetBuilder<Double> wehr2WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(5.);
	protected SetBuilder<Double> wehr3WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(5.);
	protected SetBuilder<Double> wehr4WeirDischargeOptions = new SetBuilder<Double>().add(0.).add(5.);
	
	// Ports
	
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
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new ConstantNonDeterministicExpression<>(hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkTurbineDischargeOptions);
	public Expression<Double> wehr1TurbineDischargeExpression = new ConstantNonDeterministicExpression<>(wehr1TurbineDischargeOutput, wehr1TurbineDischargeOptions);
	public Expression<Double> wehr2TurbineDischargeExpression = new ConstantNonDeterministicExpression<>(wehr2TurbineDischargeOutput, wehr2TurbineDischargeOptions);
	public Expression<Double> wehr3TurbineDischargeExpression = new ConstantNonDeterministicExpression<>(wehr3TurbineDischargeOutput, wehr3TurbineDischargeOptions);
	public Expression<Double> wehr4TurbineDischargeExpression = new ConstantNonDeterministicExpression<>(wehr4TurbineDischargeOutput, wehr4TurbineDischargeOptions);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new ConstantNonDeterministicExpression<>(hauptkraftwerkWeirDischargeOutput, hauptkraftwerkWeirDischargeOptions);
	public Expression<Double> wehr1WeirDischargeExpression = new ConstantNonDeterministicExpression<>(wehr1WeirDischargeOutput, wehr1WeirDischargeOptions);
	public Expression<Double> wehr2WeirDischargeExpression = new ConstantNonDeterministicExpression<>(wehr2WeirDischargeOutput, wehr2WeirDischargeOptions);
	public Expression<Double> wehr3WeirDischargeExpression = new ConstantNonDeterministicExpression<>(wehr3WeirDischargeOutput, wehr3WeirDischargeOptions);
	public Expression<Double> wehr4WeirDischargeExpression = new ConstantNonDeterministicExpression<>(wehr4WeirDischargeOutput, wehr4WeirDischargeOptions);

}
