package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

public class RiverComponent extends Component
{
	
	// Ports
	
	public Port<Double> scenarioInflowInput = new Port<>();
	
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
	
	public Port<Double> speicherseeLevelOutput = new Port<>();
	public Port<Double> volumen1LevelOutput = new Port<>();
	public Port<Double> volumen2LevelOutput = new Port<>();
	public Port<Double> volumen3LevelOutput = new Port<>();
	public Port<Double> volumen4LevelOutput = new Port<>();
	
	public Port<Double> hauptkraftwerkProductionOutput = new Port<>();
	public Port<Double> wehr1ProductionOutput = new Port<>();
	public Port<Double> wehr2ProductionOutput = new Port<>();
	public Port<Double> wehr3ProductionOutput = new Port<>();
	public Port<Double> wehr4ProductionOutput = new Port<>();
	
	// Components
	
	public VolumeComponent speichersee = new VolumeComponent(0, 0, 5, 3800000);
	public VolumeComponent volumen1 = new VolumeComponent(0, 0, 1.6, 13500);
	public VolumeComponent volumen2 = new VolumeComponent(0, 0, 2.1, 12525);
	public VolumeComponent volumen3 = new VolumeComponent(0, 0, 2.9, 15000);
	public VolumeComponent volumen4 = new VolumeComponent(0, 0, 3.4, 48000);
	
	public BarrageComponent hauptkraftwerk = new BarrageComponent(315, 275.4);
	public BarrageComponent wehr1 = new BarrageComponent(275.4, 271.7);
	public BarrageComponent wehr2 = new BarrageComponent(271.7, 269.2);
	public BarrageComponent wehr3 = new BarrageComponent(269.2, 267.6);
	public BarrageComponent wehr4 = new BarrageComponent(267.6, 263);
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new ChannelExpression<>(hauptkraftwerk.turbineDischargeInput, hauptkraftwerkTurbineDischargeInput);
	public Expression<Double> wehr1TurbineDischarge = new ChannelExpression<>(wehr1.turbineDischargeInput, wehr1TurbineDischargeInput);
	public Expression<Double> wehr2TurbineDischarge = new ChannelExpression<>(wehr2.turbineDischargeInput, wehr2TurbineDischargeInput);
	public Expression<Double> wehr3TurbineDischarge = new ChannelExpression<>(wehr3.turbineDischargeInput, wehr3TurbineDischargeInput);
	public Expression<Double> wehr4TurbineDischarge = new ChannelExpression<>(wehr4.turbineDischargeInput, wehr4TurbineDischargeInput);
	
	public Expression<Double> hauptkraftwerkWeirDischarge = new ChannelExpression<>(hauptkraftwerk.weirDischargeInput, hauptkraftwerkWeirDischargeInput);
	public Expression<Double> wehr1WeirDischarge = new ChannelExpression<>(wehr1.weirDischargeInput, wehr1WeirDischargeInput);
	public Expression<Double> wehr2WeirDischarge = new ChannelExpression<>(wehr2.weirDischargeInput, wehr2WeirDischargeInput);
	public Expression<Double> wehr3WeirDischarge = new ChannelExpression<>(wehr3.weirDischargeInput, wehr3WeirDischargeInput);
	public Expression<Double> wehr4WeirDischarge = new ChannelExpression<>(wehr4.weirDischargeInput, wehr4WeirDischargeInput);
	
	public Expression<Double> speicherseeInflow = new ChannelExpression<>(speichersee.inflowInput, scenarioInflowInput);
	public Expression<Double> volumen1Inflow = new ChannelExpression<>(volumen1.inflowInput, hauptkraftwerk.dischargeOutput);
	public Expression<Double> volumen2Inflow = new ChannelExpression<>(volumen2.inflowInput, wehr1.dischargeOutput);
	public Expression<Double> volumen3Inflow = new ChannelExpression<>(volumen3.inflowInput, wehr2.dischargeOutput);
	public Expression<Double> volumen4Inflow = new ChannelExpression<>(volumen4.inflowInput, wehr3.dischargeOutput);
	
	public Expression<Double> speicherseeOutflow = new ChannelExpression<>(speichersee.outflowInput, hauptkraftwerk.dischargeOutput);
	public Expression<Double> volumen1Outflow = new ChannelExpression<>(volumen1.outflowInput, wehr1.dischargeOutput);
	public Expression<Double> volumen2Outflow = new ChannelExpression<>(volumen2.outflowInput, wehr2.dischargeOutput);
	public Expression<Double> volumen3Outflow = new ChannelExpression<>(volumen3.outflowInput, wehr3.dischargeOutput);
	public Expression<Double> volumen4Outflow = new ChannelExpression<>(volumen4.outflowInput, wehr4.dischargeOutput);
	
	public Expression<Double> hauptkraftwerkHeadLevel = new ChannelExpression<>(hauptkraftwerk.headLevelInput, speichersee.levelOutput);
	public Expression<Double> wehr1HeadLevel = new ChannelExpression<>(wehr1.headLevelInput, volumen1.levelOutput);
	public Expression<Double> wehr2HeadLevel = new ChannelExpression<>(wehr2.headLevelInput, volumen2.levelOutput);
	public Expression<Double> wehr3HeadLevel = new ChannelExpression<>(wehr3.headLevelInput, volumen3.levelOutput);
	public Expression<Double> wehr4HeadLevel = new ChannelExpression<>(wehr4.headLevelInput, volumen4.levelOutput);
	
	public Expression<Double> hauptkraftwerkTailLevel = new ChannelExpression<>(hauptkraftwerk.tailLevelInput, volumen1.levelOutput);
	public Expression<Double> wehr1TailLevel = new ChannelExpression<>(wehr1.tailLevelInput, volumen2.levelOutput);
	public Expression<Double> wehr2TailLevel = new ChannelExpression<>(wehr2.tailLevelInput, volumen3.levelOutput);
	public Expression<Double> wehr3TailLevel = new ChannelExpression<>(wehr3.tailLevelInput, volumen4.levelOutput);
	public Expression<Double> wehr4TailLevel = new ConstantExpression<Double>(wehr4.tailLevelInput, 0.0);

	public Expression<Double> speicherseeLevel = new ChannelExpression<>(speicherseeLevelOutput, speichersee.levelOutput);
	public Expression<Double> volumen1Level = new ChannelExpression<>(volumen1LevelOutput, volumen1.levelOutput);
	public Expression<Double> volumen2Level = new ChannelExpression<>(volumen2LevelOutput, volumen2.levelOutput);
	public Expression<Double> volumen3Level = new ChannelExpression<>(volumen3LevelOutput, volumen3.levelOutput);
	public Expression<Double> volumen4Level = new ChannelExpression<>(volumen4LevelOutput, volumen4.levelOutput);
	
	public Expression<Double> hauptkraftwerkProduction = new ChannelExpression<>(hauptkraftwerkProductionOutput, hauptkraftwerk.productionOutput);
	public Expression<Double> wehr1Production = new ChannelExpression<>(wehr1ProductionOutput, wehr1.productionOutput);
	public Expression<Double> wehr2Production = new ChannelExpression<>(wehr2ProductionOutput, wehr2.productionOutput);
	public Expression<Double> wehr3Production = new ChannelExpression<>(wehr3ProductionOutput, wehr3.productionOutput);
	public Expression<Double> wehr4Production = new ChannelExpression<>(wehr4ProductionOutput, wehr4.productionOutput);

}
