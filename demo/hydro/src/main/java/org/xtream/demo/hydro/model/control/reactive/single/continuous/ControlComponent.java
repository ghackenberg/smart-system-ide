package org.xtream.demo.hydro.model.control.reactive.single.continuous;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.control.reactive.single.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, 0, Constants.HAUPTKRAFTWERK_DISCHARGE_MAX);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, 0, Constants.WEHR1_DISCHARGE_MAX);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, 0, Constants.WEHR2_DISCHARGE_MAX);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, 0, Constants.WEHR3_DISCHARGE_MAX);
	public Expression<Double> wehr4DischargeExpression = new DischargeLastExpression(wehr4DischargeOutput, 1, Constants.WEHR4_DISCHARGE_MAX);

}
