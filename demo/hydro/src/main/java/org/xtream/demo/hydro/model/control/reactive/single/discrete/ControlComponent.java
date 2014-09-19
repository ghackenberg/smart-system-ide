package org.xtream.demo.hydro.model.control.reactive.single.discrete;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Expression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.control.reactive.single.ControlComponent
{
	
	// Utils
	
	public static Set<Double> construct(double turbineMin, double turbineMax, double weirMax, int turbineSteps, int weirSteps)
	{
		Set<Double> result = new HashSet<>();
		
		for (double i = 0; i <= turbineSteps; i++)
		{
			result.add(turbineMin + (turbineMax - turbineMin) * i / turbineSteps);
		}
		for (double i = 1; i <= weirSteps; i++)
		{
			result.add(turbineMax + weirMax * i / weirSteps);
		}
		
		return result;
	}
	
	// Parameters
	
	public static Set<Double> hauptkraftwerkDischargeOptions = construct(0, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_TURBINE_STEPS, Constants.HAUPTKRAFTWERK_WEIR_STEPS);
	public static Set<Double> wehr1DischargeOptions = construct(0, Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_WEIR_DISCHARGE_MAX, Constants.WEHR1_TURBINE_STEPS, Constants.WEHR1_WEIR_STEPS);
	public static Set<Double> wehr2DischargeOptions = construct(0, Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_WEIR_DISCHARGE_MAX, Constants.WEHR2_TURBINE_STEPS, Constants.WEHR2_WEIR_STEPS);
	public static Set<Double> wehr3DischargeOptions = construct(0, Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_WEIR_DISCHARGE_MAX, Constants.WEHR3_TURBINE_STEPS, Constants.WEHR3_WEIR_STEPS);
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, hauptkraftwerkDischargeOptions);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, wehr1DischargeOptions);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, wehr2DischargeOptions);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, wehr3DischargeOptions);
	public Expression<Double> wehr4DischargeExpression = new DischargeLastExpression(wehr4DischargeOutput);
	
}
