package org.xtream.demo.hydro.model.split.discrete;

import java.util.Set;

import org.xtream.core.utilities.helpers.SetHelper;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Parameters
	
	public Set<Double> hauptkraftwerkTurbineDischargeOptions = SetHelper.construct(0, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, 5);
	public Set<Double> wehr1TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR1_TURBINE_DISCHARGE_MAX, 5);
	public Set<Double> wehr2TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR2_TURBINE_DISCHARGE_MAX, 5);
	public Set<Double> wehr3TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR3_TURBINE_DISCHARGE_MAX, 5);
	public Set<Double> wehr4TurbineDischargeOptions = SetHelper.construct(0, Constants.WEHR4_TURBINE_DISCHARGE_MAX, 5);

	public Set<Double> hauptkraftwerkWeirDischargeOptions = SetHelper.construct(0, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, 5);
	public Set<Double> wehr1WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR1_WEIR_DISCHARGE_MAX, 5);
	public Set<Double> wehr2WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR2_WEIR_DISCHARGE_MAX, 5);
	public Set<Double> wehr3WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR3_WEIR_DISCHARGE_MAX, 5);
	public Set<Double> wehr4WeirDischargeOptions = SetHelper.construct(0, Constants.WEHR4_WEIR_DISCHARGE_MAX, 5);
	
}
