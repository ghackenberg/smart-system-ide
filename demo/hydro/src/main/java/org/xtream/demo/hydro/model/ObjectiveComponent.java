package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;

public class ObjectiveComponent extends Component
{
	
	// Constants
	
	protected static double TURBINE_COST = 0;
	protected static double WEIR_COST = 0;
	
	// Ports

	public Port<Double> priceInput = new Port<>();
	public Port<Double> productionInput = new Port<>();
	
	public Port<Double> hauptkraftwerkTurbineDischargeInput = new Port<>();
	public Port<Double> wehr1TurbineDischargeInput = new Port<>();
	public Port<Double> wehr2TurbineDischargeInput = new Port<>();
	public Port<Double> wehr3TurbineDischargeInput = new Port<>();
	public Port<Double> wehr4TurbineDischargeInput = new Port<>();
	
	public Port<Double> hauptkraftwerkWeirDischargeInput = new Port<>();
	public Port<Double> wehr1WeirDischargeInput = new Port<>();
	public Port<Double> wehr2WeirDischargeInput = new Port<>();
	public Port<Double> wehr3WeirDischargeInput = new Port<>();
	public Port<Double> wehr4WeirDischargeInput = new Port<>();

	public Port<Double> objectiveOutput = new Port<>();
	public Port<Double> rewardOutput = new Port<>();
	public Port<Double> costOutput = new Port<>();
	
	// Charts
	
	public Chart objectiveChart = new Timeline(rewardOutput, costOutput, objectiveOutput);
	
	// Expressions
	
	public Expression<Double> rewardExpression = new Expression<Double>(rewardOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : rewardOutput.get(state, timepoint - 1)) + productionInput.get(state, timepoint) * priceInput.get(state, timepoint);
		}
	};
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.0;
			}
			else
			{
				double sum = 0;
				
				if (hauptkraftwerkTurbineDischargeInput.get(state, timepoint - 1) != hauptkraftwerkTurbineDischargeInput.get(state, timepoint) || hauptkraftwerkWeirDischargeInput.get(state, timepoint - 1) != hauptkraftwerkWeirDischargeInput.get(state, timepoint))
				{
					sum += Constants.HAUPTKRAFTWERK_DISCHARGE_CHANGE_COST;
				}
				if (wehr1TurbineDischargeInput.get(state, timepoint - 1) != wehr1TurbineDischargeInput.get(state, timepoint) || wehr1WeirDischargeInput.get(state, timepoint - 1) != wehr1WeirDischargeInput.get(state, timepoint))
				{
					sum += Constants.WEHR1_DISCHARGE_CHANGE_COST;
				}
				if (wehr2TurbineDischargeInput.get(state, timepoint - 1) != wehr2TurbineDischargeInput.get(state, timepoint) || wehr2WeirDischargeInput.get(state, timepoint - 1) != wehr2WeirDischargeInput.get(state, timepoint))
				{
					sum += Constants.WEHR2_DISCHARGE_CHANGE_COST;
				}
				if (wehr3TurbineDischargeInput.get(state, timepoint - 1) != wehr3TurbineDischargeInput.get(state, timepoint) || wehr3WeirDischargeInput.get(state, timepoint - 1) != wehr3WeirDischargeInput.get(state, timepoint))
				{
					sum += Constants.WEHR3_DISCHARGE_CHANGE_COST;
				}
				if (wehr4TurbineDischargeInput.get(state, timepoint - 1) != wehr4TurbineDischargeInput.get(state, timepoint) || wehr4WeirDischargeInput.get(state, timepoint - 1) != wehr4WeirDischargeInput.get(state, timepoint))
				{
					sum += Constants.WEHR4_DISCHARGE_CHANGE_COST;
				}
				
				return costOutput.get(state, timepoint - 1) - sum;
			}
		}

	};
	public Expression<Double> objectiveExpression = new Expression<Double>(objectiveOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return rewardOutput.get(state, timepoint) + costOutput.get(state, timepoint);
		}

	};
	
}
