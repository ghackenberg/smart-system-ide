package org.xtream.demo.hydro.model.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.continuous.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, Constants.WEHR1_TURBINE_DISCHARGE_MAX);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, Constants.WEHR2_TURBINE_DISCHARGE_MAX);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, Constants.WEHR3_TURBINE_DISCHARGE_MAX);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, Constants.WEHR4_TURBINE_DISCHARGE_MAX);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, Constants.WEHR1_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, Constants.WEHR2_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, Constants.WEHR3_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, Constants.WEHR4_WEIR_DISCHARGE_MAX);

}
