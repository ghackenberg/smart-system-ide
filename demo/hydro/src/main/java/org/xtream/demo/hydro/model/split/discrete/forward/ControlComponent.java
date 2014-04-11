package org.xtream.demo.hydro.model.split.discrete.forward;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.split.discrete.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, hauptkraftwerkDischargeOptions, speicherseeLevelInput, Constants.SPEICHERSEE_AREA, Constants.SPEICHERSEE_LEVEL_MAX, scenarioInflowInput);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, wehr1DischargeOptions, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, hauptkraftwerkDischargeOutput);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, wehr2DischargeOptions, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr1DischargeOutput);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, wehr3DischargeOptions, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr2DischargeOutput);
	public Expression<Double> wehr4DischargeExpression = new DischargeExpression(wehr4DischargeOutput, wehr4DischargeOptions, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr3DischargeOutput);

}
