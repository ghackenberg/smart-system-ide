package org.xtream.demo.hydro.model.single.continuous.random;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.single.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, Constants.HAUPTKRAFTWERK_DISCHARGE_MAX);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, Constants.WEHR1_DISCHARGE_MAX);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, Constants.WEHR2_DISCHARGE_MAX);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, Constants.WEHR3_DISCHARGE_MAX);
	public Expression<Double> wehr4DischargeExpression = new DischargeExpression(wehr4DischargeOutput, Constants.WEHR4_DISCHARGE_MAX);

}
