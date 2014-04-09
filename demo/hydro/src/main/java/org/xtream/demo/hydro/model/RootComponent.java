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
		new Workbench<>(RootComponent.class, 96, 100, 10, 0.5);
	}
	
	// Components
	
	public ControlComponent steuerung = new ControlComponent();
	public ContextComponent context = new ContextComponent();
	public ObjectiveComponent objective = new ObjectiveComponent();
	
	// Charts

	public Chart inflowChart = new Chart(context.scenarioInflowOutput);
	public Chart priceChart = new Chart(context.scenarioPriceOutput);
	public Chart productionChart = new Chart(context.netProductionOutput);
	public Chart rewardChart = new Chart(objective.rewardOutput);
	
	// Expressions
	
	public Expression<Double> speicherseeLevel = new ChannelExpression<>(steuerung.speicherseeLevelInput, context.speicherseeLevelOutput);
	public Expression<Double> volumen1Level = new ChannelExpression<>(steuerung.volumen1LevelInput, context.volumen1LevelOutput);
	public Expression<Double> volumen2Level = new ChannelExpression<>(steuerung.volumen2LevelInput, context.volumen2LevelOutput);
	public Expression<Double> volumen3Level = new ChannelExpression<>(steuerung.volumen3LevelInput, context.volumen3LevelOutput);
	public Expression<Double> volumen4Level = new ChannelExpression<>(steuerung.volumen4LevelInput, context.volumen4LevelOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new ChannelExpression<>(context.hauptkraftwerkTurbineDischargeInput, steuerung.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischarge = new ChannelExpression<>(context.wehr1TurbineDischargeInput, steuerung.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischarge = new ChannelExpression<>(context.wehr2TurbineDischargeInput, steuerung.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischarge = new ChannelExpression<>(context.wehr3TurbineDischargeInput, steuerung.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischarge = new ChannelExpression<>(context.wehr4TurbineDischargeInput, steuerung.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischarge = new ChannelExpression<>(context.hauptkraftwerkWeirDischargeInput, steuerung.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischarge = new ChannelExpression<>(context.wehr1WeirDischargeInput, steuerung.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischarge = new ChannelExpression<>(context.wehr2WeirDischargeInput, steuerung.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischarge = new ChannelExpression<>(context.wehr3WeirDischargeInput, steuerung.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischarge = new ChannelExpression<>(context.wehr4WeirDischargeInput, steuerung.wehr4WeirDischargeOutput);
	
	
	public Expression<Double> price = new ChannelExpression<>(objective.priceInput, context.scenarioPriceOutput);
	public Expression<Double> production = new ChannelExpression<>(objective.productionInput, context.netProductionOutput);

}
