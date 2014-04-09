package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;

public class ContextComponent extends Component
{
	
	// Ports
	
	public Port<Double> scenarioInflowOutput = new Port<>();
	public Port<Double> scenarioPriceOutput = new Port<>();
	
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
	
	public Port<Double> netProductionOutput = new Port<>();
	
	// Components
	
	public ScenarioComponent scenario = new ScenarioComponent(ScenarioComponent.INFLOW_1, ScenarioComponent.PRICE_1);
	public RiverComponent river = new RiverComponent();
	public NetComponent net = new NetComponent();
	
	// Channels
	
	public Expression<Double> scenarioInflowToEnvironment = new ChannelExpression<>(scenarioInflowOutput, scenario.inflowOutput);
	public Expression<Double> scenarioInflowToRiver = new ChannelExpression<>(river.scenarioInflowInput, scenario.inflowOutput);
	public Expression<Double> scenarioPrice= new ChannelExpression<>(scenarioPriceOutput, scenario.priceOutput);
	
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

}
