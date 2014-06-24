package org.xtream.demo.hydro.model.single.discrete;

import java.util.HashSet;
import java.util.Set;

import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.single.ControlComponent
{
	
	// Utils

	public static Set<Double> construct(double turbineMax, double weirMax, int turbineSteps, int weirSteps)
	{
		Set<Double> result = new HashSet<>();
		
		for (double i = 0; i <= turbineSteps; i++)
		{
			result.add(turbineMax * i / turbineSteps);
		}
		for (double i = 1; i <= weirSteps; i++)
		{
			result.add(turbineMax + weirMax * i / weirSteps);
		}
		
		return result;
	}
	
	// Parameters
	
	public int TURBINE_STEPS = 5;
	public int WEIR_STEPS = 3;
	
	public Set<Double> hauptkraftwerkDischargeOptions = construct(Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, TURBINE_STEPS, WEIR_STEPS);
	public Set<Double> wehr1DischargeOptions = construct(Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_WEIR_DISCHARGE_MAX, TURBINE_STEPS, WEIR_STEPS);
	public Set<Double> wehr2DischargeOptions = construct(Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_WEIR_DISCHARGE_MAX, TURBINE_STEPS, WEIR_STEPS);
	public Set<Double> wehr3DischargeOptions = construct(Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_WEIR_DISCHARGE_MAX, TURBINE_STEPS, WEIR_STEPS);
	public Set<Double> wehr4DischargeOptions = construct(Constants.WEHR4_TURBINE_DISCHARGE_MAX, Constants.WEHR4_WEIR_DISCHARGE_MAX, TURBINE_STEPS, WEIR_STEPS);
	
}
