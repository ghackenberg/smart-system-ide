package org.xtream.demo.hydro.model.single.discrete;

import java.util.Set;

import org.xtream.core.model.helpers.SetHelper;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.single.ControlComponent
{
	
	// Parameters
	
	public int STEPS = 7;
	
	public Set<Double> hauptkraftwerkDischargeOptions = SetHelper.construct(0, Constants.HAUPTKRAFTWERK_DISCHARGE_MAX, STEPS);
	public Set<Double> wehr1DischargeOptions = SetHelper.construct(0, Constants.WEHR1_DISCHARGE_MAX, STEPS);
	public Set<Double> wehr2DischargeOptions = SetHelper.construct(0, Constants.WEHR2_DISCHARGE_MAX, STEPS);
	public Set<Double> wehr3DischargeOptions = SetHelper.construct(0, Constants.WEHR3_DISCHARGE_MAX, STEPS);
	public Set<Double> wehr4DischargeOptions = SetHelper.construct(0, Constants.WEHR4_DISCHARGE_MAX, STEPS);
	
}
