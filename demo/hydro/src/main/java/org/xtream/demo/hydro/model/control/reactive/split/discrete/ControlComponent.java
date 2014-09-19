package org.xtream.demo.hydro.model.control.reactive.split.discrete;

import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.utilities.helpers.SetHelper;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Parameters
	
	public Set<Double> hauptkraftwerkTurbineDischargeOptions = SetHelper.construct(0, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_TURBINE_STEPS);
	public Set<Double> wehr1TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_TURBINE_STEPS);
	public Set<Double> wehr2TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_TURBINE_STEPS);
	public Set<Double> wehr3TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_TURBINE_STEPS);
	public Set<Double> wehr4TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR4_TURBINE_DISCHARGE_MAX, Constants.WEHR4_TURBINE_STEPS);

	public Set<Double> hauptkraftwerkWeirDischargeOptions = SetHelper.construct(0, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_STEPS);
	public Set<Double> wehr1WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR1_WEIR_DISCHARGE_MAX, Constants.WEHR1_WEIR_STEPS);
	public Set<Double> wehr2WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR2_WEIR_DISCHARGE_MAX, Constants.WEHR2_WEIR_STEPS);
	public Set<Double> wehr3WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR3_WEIR_DISCHARGE_MAX, Constants.WEHR3_WEIR_STEPS);
	public Set<Double> wehr4WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR4_WEIR_DISCHARGE_MAX, Constants.WEHR4_WEIR_STEPS);
	
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
