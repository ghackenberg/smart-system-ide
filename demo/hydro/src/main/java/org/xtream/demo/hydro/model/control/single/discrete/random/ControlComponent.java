package org.xtream.demo.hydro.model.control.single.discrete.random;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ControlComponent extends org.xtream.demo.hydro.model.control.single.discrete.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new ConstantNonDeterministicExpression<>(hauptkraftwerkDischargeOutput, hauptkraftwerkDischargeOptions);
	public Expression<Double> wehr1DischargeExpression = new ConstantNonDeterministicExpression<>(wehr1DischargeOutput, wehr1DischargeOptions);
	public Expression<Double> wehr2DischargeExpression = new ConstantNonDeterministicExpression<>(wehr2DischargeOutput, wehr2DischargeOptions);
	public Expression<Double> wehr3DischargeExpression = new ConstantNonDeterministicExpression<>(wehr3DischargeOutput, wehr3DischargeOptions);
	public Expression<Double> wehr4DischargeExpression = new ConstantNonDeterministicExpression<>(wehr4DischargeOutput, wehr4DischargeOptions);

}
