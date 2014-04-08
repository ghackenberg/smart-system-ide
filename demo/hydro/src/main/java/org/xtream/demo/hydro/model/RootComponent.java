package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Workbench<>(RootComponent.class, 96, 1000, 20, 0.5);
	}
	
	// Components
	
	public ScenarioComponent scenario = new ScenarioComponent(ScenarioComponent.INFLOW_1, ScenarioComponent.PRICE_1);
	
	public ControlComponent steuerung = new ControlComponent();
	
	public VolumeComponent speichersee = new VolumeComponent(0, 0, 5, 3800000);
	public VolumeComponent volumen1 = new VolumeComponent(0, 0, 1.6, 13500);
	public VolumeComponent volumen2 = new VolumeComponent(0, 0, 2.1, 12525);
	public VolumeComponent volumen3 = new VolumeComponent(0, 0, 2.9, 15000);
	public VolumeComponent volumen4 = new VolumeComponent(0, 0, 3.4, 48000);
	public ConstantVolumeComponent volumen5 = new ConstantVolumeComponent(0);
	
	public BarrageComponent hauptkraftwerk = new BarrageComponent(315, 275.4);
	public BarrageComponent wehr1 = new BarrageComponent(275.4, 271.7);
	public BarrageComponent wehr2 = new BarrageComponent(271.7, 269.2);
	public BarrageComponent wehr3 = new BarrageComponent(269.2, 267.6);
	public BarrageComponent wehr4 = new BarrageComponent(267.6, 263);
	
	public NetComponent net = new NetComponent();
	
	public ObjectiveComponent objective = new ObjectiveComponent();
	
	// Charts

	public Chart inflowChart = new Chart(scenario.inflowOutput);
	public Chart priceChart = new Chart(scenario.priceOutput);
	public Chart productionChart = new Chart(net.productionOutput);
	public Chart rewardChart = new Chart(objective.rewardOutput);
	
	// Expressions
	
	public Expression<Double> speicherseeLevel = new ChannelExpression<>(steuerung.speicherseeLevelInput, speichersee.levelOutput);
	public Expression<Double> volumen1Level = new ChannelExpression<>(steuerung.volumen1LevelInput, volumen1.levelOutput);
	public Expression<Double> volumen2Level = new ChannelExpression<>(steuerung.volumen2LevelInput, volumen2.levelOutput);
	public Expression<Double> volumen3Level = new ChannelExpression<>(steuerung.volumen3LevelInput, volumen3.levelOutput);
	public Expression<Double> volumen4Level = new ChannelExpression<>(steuerung.volumen4LevelInput, volumen4.levelOutput);
	
	public Expression<Double> speicherseeInflow = new ChannelExpression<>(speichersee.inflowInput, scenario.inflowOutput);
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
	public Expression<Double> wehr4TailLevel = new ChannelExpression<>(wehr4.tailLevelInput, volumen5.levelOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new ChannelExpression<>(hauptkraftwerk.turbineDischargeInput, steuerung.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischarge = new ChannelExpression<>(wehr1.turbineDischargeInput, steuerung.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischarge = new ChannelExpression<>(wehr2.turbineDischargeInput, steuerung.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischarge = new ChannelExpression<>(wehr3.turbineDischargeInput, steuerung.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischarge = new ChannelExpression<>(wehr4.turbineDischargeInput, steuerung.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischarge = new ChannelExpression<>(hauptkraftwerk.weirDischargeInput, steuerung.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischarge = new ChannelExpression<>(wehr1.weirDischargeInput, steuerung.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischarge = new ChannelExpression<>(wehr2.weirDischargeInput, steuerung.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischarge = new ChannelExpression<>(wehr3.weirDischargeInput, steuerung.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischarge = new ChannelExpression<>(wehr4.weirDischargeInput, steuerung.wehr4WeirDischargeOutput);
	
	public Expression<Double> hauptkraftwerkProduction = new ChannelExpression<>(net.hauptkraftwerkProductionInput, hauptkraftwerk.productionOutput);
	public Expression<Double> wehr1Production = new ChannelExpression<>(net.wehr1ProductionInput, wehr1.productionOutput);
	public Expression<Double> wehr2Production = new ChannelExpression<>(net.wehr2ProductionInput, wehr2.productionOutput);
	public Expression<Double> wehr3Production = new ChannelExpression<>(net.wehr3ProductionInput, wehr3.productionOutput);
	public Expression<Double> wehr4Production = new ChannelExpression<>(net.wehr4ProductionInput, wehr4.productionOutput);
	
	public Expression<Double> price = new ChannelExpression<>(objective.priceInput, scenario.priceOutput);
	public Expression<Double> production = new ChannelExpression<>(objective.productionInput, net.productionOutput);

}
