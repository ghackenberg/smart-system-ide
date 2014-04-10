package org.xtream.demo.hydro.model.discrete.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ControlComponent extends org.xtream.demo.hydro.model.discrete.ControlComponent
{
	
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
