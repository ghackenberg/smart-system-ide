package org.xtream.demo.hydro.model.control.actual;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new Expression<Double>(hauptkraftwerkTurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> hauptkraftwerkWeirDischarge = new Expression<Double>(hauptkraftwerkWeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(0, Constants.START + timepoint) + Constants.DATASET.getOutflowTurbine(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr1TurbineDischarge = new Expression<Double>(wehr1TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(1, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr1WeirDischarge = new Expression<Double>(wehr1WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(1, Constants.START + timepoint) + Constants.DATASET.getOutflowTurbine(1, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr2TurbineDischarge = new Expression<Double>(wehr2TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(2, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr2WeirDischarge = new Expression<Double>(wehr2WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(2, Constants.START + timepoint) + Constants.DATASET.getOutflowTurbine(2, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr3TurbineDischarge = new Expression<Double>(wehr3TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(3, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr3WeirDischarge = new Expression<Double>(wehr3WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(3, Constants.START + timepoint) + Constants.DATASET.getOutflowTurbine(3, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr4TurbineDischarge = new Expression<Double>(wehr4TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTurbine(4, Constants.START + timepoint);
		}
	};
	public Expression<Double> wehr4WeirDischarge = new Expression<Double>(wehr4WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getOutflowTotal(4, Constants.START + timepoint) + Constants.DATASET.getOutflowTurbine(4, Constants.START + timepoint);
		}
	};
	
}
