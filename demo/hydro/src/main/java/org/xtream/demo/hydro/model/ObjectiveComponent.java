package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.optimizer.State;

public class ObjectiveComponent extends Component
{
	
	// Constants
	
	protected static double TURBINE_COST = 100;
	protected static double WEIR_COST = 500;
	
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
	
	// Objectives
	
	public Objective objective = new MaxObjective(objectiveOutput);
	
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
				
				// hauptkraftwerk turbine
				{
					double diff = hauptkraftwerkTurbineDischargeInput.get(state, timepoint - 1) - hauptkraftwerkTurbineDischargeInput.get(state, timepoint);
					sum += diff * diff * TURBINE_COST;
				}
				// wehr1 turbine
				{
					double diff = wehr1TurbineDischargeInput.get(state, timepoint - 1) - wehr1TurbineDischargeInput.get(state, timepoint);
					sum += diff * diff * TURBINE_COST;
				}
				// wehr2 turbine
				{
					double diff = wehr2TurbineDischargeInput.get(state, timepoint - 1) - wehr2TurbineDischargeInput.get(state, timepoint);
					sum += diff * diff * TURBINE_COST;
				}
				// wehr3 turbine
				{
					double diff = wehr3TurbineDischargeInput.get(state, timepoint - 1) - wehr3TurbineDischargeInput.get(state, timepoint);
					sum += diff * diff * TURBINE_COST;
				}
				// wehr4 turbine
				{
					double diff = wehr4TurbineDischargeInput.get(state, timepoint - 1) - wehr4TurbineDischargeInput.get(state, timepoint);
					sum += diff * diff * TURBINE_COST;
				}
				
				// hauptkraftwerk weir
				{
					double diff = hauptkraftwerkWeirDischargeInput.get(state, timepoint - 1) - hauptkraftwerkWeirDischargeInput.get(state, timepoint);
					sum += diff * diff * WEIR_COST;
				}
				// wehr1 weir
				{
					double diff = wehr1WeirDischargeInput.get(state, timepoint - 1) - wehr1WeirDischargeInput.get(state, timepoint);
					sum += diff * diff * WEIR_COST;
				}
				// wehr2 weir
				{
					double diff = wehr2WeirDischargeInput.get(state, timepoint - 1) - wehr2WeirDischargeInput.get(state, timepoint);
					sum += diff * diff * WEIR_COST;
				}
				// wehr3 weir
				{
					double diff = wehr3WeirDischargeInput.get(state, timepoint - 1) - wehr3WeirDischargeInput.get(state, timepoint);
					sum += diff * diff * WEIR_COST;
				}
				// wehr4 weir
				{
					double diff = wehr4WeirDischargeInput.get(state, timepoint - 1) - wehr4WeirDischargeInput.get(state, timepoint);
					sum += diff * diff * WEIR_COST;
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
