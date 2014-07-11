package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.State;

public class ContextComponent extends Component
{
	
	// Ports
	
	public Port<Double> inflowInput = new Port<>();
	
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
	
	public Port<Double> hauptkraftwerkTurbineDischargeOutput = new Port<>();
	public Port<Double> wehr1TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr2TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr3TurbineDischargeOutput = new Port<>();
	public Port<Double> wehr4TurbineDischargeOutput = new Port<>();
	
	public Port<Double> hauptkraftwerkWeirDischargeOutput = new Port<>();
	public Port<Double> wehr1WeirDischargeOutput = new Port<>();
	public Port<Double> wehr2WeirDischargeOutput = new Port<>();
	public Port<Double> wehr3WeirDischargeOutput = new Port<>();
	public Port<Double> wehr4WeirDischargeOutput = new Port<>();
	
	public Port<Double> speicherseeLevelOutput = new Port<>();
	public Port<Double> volumen1LevelOutput = new Port<>();
	public Port<Double> volumen2LevelOutput = new Port<>();
	public Port<Double> volumen3LevelOutput = new Port<>();
	public Port<Double> volumen4LevelOutput = new Port<>();
	
	public Port<Double> netProductionOutput = new Port<>();
	
	// Components
	
	public RiverComponent river = new RiverComponent();
	public NetComponent net = new NetComponent();
	
	// Channels
	
	public Expression<Double> scenarioInflow = new ChannelExpression<>(river.scenarioInflowInput, inflowInput);
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new ChannelExpression<>(river.hauptkraftwerkTurbineDischargeInput, hauptkraftwerkTurbineDischargeInput);
	public Expression<Double> wehr1TurbineDischarge = new ChannelExpression<>(river.wehr1TurbineDischargeInput, wehr1TurbineDischargeInput);
	public Expression<Double> wehr2TurbineDischarge = new ChannelExpression<>(river.wehr2TurbineDischargeInput, wehr2TurbineDischargeInput);
	public Expression<Double> wehr3TurbineDischarge = new ChannelExpression<>(river.wehr3TurbineDischargeInput, wehr3TurbineDischargeInput);
	public Expression<Double> wehr4TurbineDischarge = new ChannelExpression<>(river.wehr4TurbineDischargeInput, wehr4TurbineDischargeInput);
	
	public Expression<Double> hauptkraftwerkWeirDischarge = new ChannelExpression<>(river.hauptkraftwerkWeirDischargeInput, hauptkraftwerkWeirDischargeInput);
	public Expression<Double> wehr1WeirDischarge = new ChannelExpression<>(river.wehr1WeirDischargeInput, wehr1WeirDischargeInput);
	public Expression<Double> wehr2WeirDischarge = new ChannelExpression<>(river.wehr2WeirDischargeInput, wehr2WeirDischargeInput);
	public Expression<Double> wehr3WeirDischarge = new ChannelExpression<>(river.wehr3WeirDischargeInput, wehr3WeirDischargeInput);
	public Expression<Double> wehr4WeirDischarge = new ChannelExpression<>(river.wehr4WeirDischargeInput, wehr4WeirDischargeInput);
	
	public Expression<Double> hauptkraftwerkProduction = new ChannelExpression<>(net.hauptkraftwerkProductionInput, river.hauptkraftwerkProductionOutput);
	public Expression<Double> wehr1Production = new ChannelExpression<>(net.wehr1ProductionInput, river.wehr1ProductionOutput);
	public Expression<Double> wehr2Production = new ChannelExpression<>(net.wehr2ProductionInput, river.wehr2ProductionOutput);
	public Expression<Double> wehr3Production = new ChannelExpression<>(net.wehr3ProductionInput, river.wehr3ProductionOutput);
	public Expression<Double> wehr4Production = new ChannelExpression<>(net.wehr4ProductionInput, river.wehr4ProductionOutput);

	public Expression<Double> speicherseeLevel = new ChannelExpression<>(speicherseeLevelOutput, river.speicherseeLevelOutput);
	public Expression<Double> volumen1Level = new ChannelExpression<>(volumen1LevelOutput, river.volumen1LevelOutput);
	public Expression<Double> volumen2Level = new ChannelExpression<>(volumen2LevelOutput, river.volumen2LevelOutput);
	public Expression<Double> volumen3Level = new ChannelExpression<>(volumen3LevelOutput, river.volumen3LevelOutput);
	public Expression<Double> volumen4Level = new ChannelExpression<>(volumen4LevelOutput, river.volumen4LevelOutput);
	
	public Expression<Double> netProduction = new ChannelExpression<>(netProductionOutput, net.productionOutput);
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpr = new Expression<Double>(hauptkraftwerkTurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return hauptkraftwerkTurbineDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr1TurbineDischargeExpr = new Expression<Double>(wehr1TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr1TurbineDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr2TurbineDischargeExpr = new Expression<Double>(wehr2TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr2TurbineDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr3TurbineDischargeExpr = new Expression<Double>(wehr3TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr3TurbineDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr4TurbineDischargeExpr = new Expression<Double>(wehr4TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr4TurbineDischargeInput.get(state, timepoint);
		}
	};

	public Expression<Double> hauptkraftwerkWeirDischargeExpr = new Expression<Double>(hauptkraftwerkWeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return hauptkraftwerkWeirDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr1WeirDischargeExpr = new Expression<Double>(wehr1WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr1WeirDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr2WeirDischargeExpr = new Expression<Double>(wehr2WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr2WeirDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr3WeirDischargeExpr = new Expression<Double>(wehr3WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr3WeirDischargeInput.get(state, timepoint);
		}
	};
	public Expression<Double> wehr4WeirDischargeExpr = new Expression<Double>(wehr4WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return wehr4WeirDischargeInput.get(state, timepoint);
		}
	};

}
