package org.xtream.demo.hydro.model.control.single.discrete;

import java.util.HashSet;
import java.util.Set;

import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.control.single.ControlComponent
{
	
	// Utils

	public static Set<Double> construct(double turbineMax, double weirMax, int turbineSteps, int weirSteps)
	{
		return construct(0., turbineMax, weirMax, turbineSteps, weirSteps);
	}
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
	
	public static int HAUPTKRAFTWERK_TURBINE_STEPS = 2;
	public static int WEHR1_TURBINE_STEPS = 2;
	public static int WEHR2_TURBINE_STEPS = 2;
	public static int WEHR3_TURBINE_STEPS = 2;
	public static int WEHR4_TURBINE_STEPS = 10;
	public static int WEIR_STEPS = 10;
	
	public static Set<Double> hauptkraftwerkDischargeOptions = construct(Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, HAUPTKRAFTWERK_TURBINE_STEPS, WEIR_STEPS);
	public static Set<Double> wehr1DischargeOptions = construct(Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_WEIR_DISCHARGE_MAX, WEHR1_TURBINE_STEPS, WEIR_STEPS);
	public static Set<Double> wehr2DischargeOptions = construct(Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_WEIR_DISCHARGE_MAX, WEHR2_TURBINE_STEPS, WEIR_STEPS);
	public static Set<Double> wehr3DischargeOptions = construct(Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_WEIR_DISCHARGE_MAX, WEHR3_TURBINE_STEPS, WEIR_STEPS);
	public static Set<Double> wehr4DischargeOptions = construct(1, Constants.WEHR4_TURBINE_DISCHARGE_MAX, Constants.WEHR4_WEIR_DISCHARGE_MAX, WEHR4_TURBINE_STEPS, WEIR_STEPS);
	
}
