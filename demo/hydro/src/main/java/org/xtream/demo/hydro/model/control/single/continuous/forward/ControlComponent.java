package org.xtream.demo.hydro.model.control.single.continuous.forward;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.control.single.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, Constants.HAUPTKRAFTWERK_DISCHARGE_MAX, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, inflowInput);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, Constants.WEHR1_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkDischargeOutput);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, Constants.WEHR2_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1DischargeOutput);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, Constants.WEHR3_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2DischargeOutput);
	public Expression<Double> wehr4DischargeExpression = new DischargeExpression(wehr4DischargeOutput, Constants.WEHR4_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3DischargeOutput);

}
