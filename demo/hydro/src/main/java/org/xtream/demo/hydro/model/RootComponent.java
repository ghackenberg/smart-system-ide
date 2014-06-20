package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new RootComponent(), 96 * 7, 100, 100, 0, 0);
	}
	
	// Components
	
	public ControlComponent control = new org.xtream.demo.hydro.model.single.continuous.backward.ControlComponent();
	public ContextComponent context = new ContextComponent();
	public ObjectiveComponent objective = new ObjectiveComponent();
	public EquivalenceComponent equivalence = new EquivalenceComponent();
	
	// Charts

	public Chart inflowChart = new Timeline(context.scenarioInflowOutput);
	public Chart priceChart = new Timeline(context.scenarioPriceOutput);
	public Chart productionChart = new Timeline(context.netProductionOutput, context.scenarioPriceOutput);
	public Chart rewardChart = new Timeline(objective.rewardOutput);
	
	// Expressions
	
	public Expression<Double> scenarioInflow = new ChannelExpression<>(control.scenarioInflowInput, context.scenarioInflowOutput);
	public Expression<Double> scenarioPrice= new ChannelExpression<>(control.scenarioPriceInput, context.scenarioPriceOutput);
	
	public Expression<Double> speicherseeLevelToControl = new ChannelExpression<>(control.speicherseeLevelInput, context.speicherseeLevelOutput);
	public Expression<Double> volumen1LevelToControl = new ChannelExpression<>(control.volumen1LevelInput, context.volumen1LevelOutput);
	public Expression<Double> volumen2LevelToControl = new ChannelExpression<>(control.volumen2LevelInput, context.volumen2LevelOutput);
	public Expression<Double> volumen3LevelToControl = new ChannelExpression<>(control.volumen3LevelInput, context.volumen3LevelOutput);
	public Expression<Double> volumen4LevelToControl = new ChannelExpression<>(control.volumen4LevelInput, context.volumen4LevelOutput);
	
	public Expression<Double> speicherseeLevelToEquivalence = new ChannelExpression<>(equivalence.speicherseeLevelInput, context.speicherseeLevelOutput);
	public Expression<Double> volumen1LevelToEquivalence = new ChannelExpression<>(equivalence.volumen1LevelInput, context.volumen1LevelOutput);
	public Expression<Double> volumen2LevelToEquivalence = new ChannelExpression<>(equivalence.volumen2LevelInput, context.volumen2LevelOutput);
	public Expression<Double> volumen3LevelToEquivalence = new ChannelExpression<>(equivalence.volumen3LevelInput, context.volumen3LevelOutput);
	public Expression<Double> volumen4LevelToEquivalence = new ChannelExpression<>(equivalence.volumen4LevelInput, context.volumen4LevelOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new ChannelExpression<>(context.hauptkraftwerkTurbineDischargeInput, control.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischarge = new ChannelExpression<>(context.wehr1TurbineDischargeInput, control.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischarge = new ChannelExpression<>(context.wehr2TurbineDischargeInput, control.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischarge = new ChannelExpression<>(context.wehr3TurbineDischargeInput, control.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischarge = new ChannelExpression<>(context.wehr4TurbineDischargeInput, control.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischarge = new ChannelExpression<>(context.hauptkraftwerkWeirDischargeInput, control.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischarge = new ChannelExpression<>(context.wehr1WeirDischargeInput, control.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischarge = new ChannelExpression<>(context.wehr2WeirDischargeInput, control.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischarge = new ChannelExpression<>(context.wehr3WeirDischargeInput, control.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischarge = new ChannelExpression<>(context.wehr4WeirDischargeInput, control.wehr4WeirDischargeOutput);
	
	
	public Expression<Double> price = new ChannelExpression<>(objective.priceInput, context.scenarioPriceOutput);
	public Expression<Double> production = new ChannelExpression<>(objective.productionInput, context.netProductionOutput);

}
