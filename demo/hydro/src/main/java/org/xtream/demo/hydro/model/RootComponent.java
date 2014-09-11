package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	// Main
	
	public static void main(String[] args)
	{
		new Workbench<>(new RootComponent(), DURATION, SAMPLES, CLUSTERS, RANDOM, CACHING, ROUNDS);
	}
	
	// Constants
	
	public static String WEEK_1 = "csv/All_extended_week_2_2011.csv";
	public static String WEEK_2 = "csv/All_extended_week_6_2011.csv";
	public static String WEEK_3 = "csv/All_extended_week_14_2011.csv";
	public static String WEEK_4 = "csv/All_extended_week_24_2011.csv";

	public static String INFLOW_1 = "csv/Inflow_week_2_2011.csv";
	public static String INFLOW_2 = "csv/Inflow_week_6_2011.csv";
	public static String INFLOW_3 = "csv/Inflow_week_14_2011.csv";
	public static String INFLOW_4 = "csv/Inflow_week_24_2011.csv";
	
	public static String PRICE_1 = "csv/Price_week_2_2011.csv";
	public static String PRICE_2 = "csv/Price_week_6_2011.csv";
	public static String PRICE_3 = "csv/Price_week_14_2011.csv";
	public static String PRICE_4 = "csv/Price_week_24_2011.csv";
	
	public static int DURATION = 96 * 7;
	public static int SAMPLES = 200;
	public static int CLUSTERS = 50;
	public static double RANDOM = 0;
	public static double CACHING = 0;
	public static int ROUNDS = 1000;
	
	// Components

	public ScenarioComponent scenario = new ScenarioComponent(INFLOW_1, PRICE_1);
	//public ControlComponent control = new org.xtream.demo.hydro.model.actual.ControlComponent(WEEK_1);
	//public ControlComponent control = new org.xtream.demo.hydro.model.single.continuous.backward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.single.continuous.forward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.single.continuous.random.ControlComponent();
	public ControlComponent control = new org.xtream.demo.hydro.model.single.discrete.backward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.single.discrete.forward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.single.discrete.random.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.continuous.backward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.continuous.forward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.continuous.random.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.discrete.backward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.discrete.forward.ControlComponent();
	//public ControlComponent control = new org.xtream.demo.hydro.model.split.discrete.random.ControlComponent();
	public ContextComponent context = new ContextComponent();
	public ObjectiveComponent objective = new ObjectiveComponent();
	public EquivalenceComponent equivalence = new EquivalenceComponent();
	
	// Charts

	public Chart inflowChart = new Timeline(scenario.inflowOutput);
	public Chart priceChart = new Timeline(scenario.priceOutput);
	public Chart productionChart = new Timeline(context.netProductionOutput);
	public Chart objectiveChart = new Timeline(objective.rewardOutput, objective.costOutput, objective.objectiveOutput);
	
	// Expressions
 
	public Expression<Double> inflowToContext = new ChannelExpression<>(context.inflowInput, scenario.inflowOutput);
	
	public Expression<Double> inflowToControl = new ChannelExpression<>(control.inflowInput, scenario.inflowOutput);
	public Expression<Double> priceToControl = new ChannelExpression<>(control.priceInput, scenario.priceOutput);
	
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
	
	public Expression<Double> hauptkraftwerkTurbineDischargeToContext = new ChannelExpression<>(context.hauptkraftwerkTurbineDischargeInput, control.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeToContext = new ChannelExpression<>(context.wehr1TurbineDischargeInput, control.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeToContext = new ChannelExpression<>(context.wehr2TurbineDischargeInput, control.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeToContext = new ChannelExpression<>(context.wehr3TurbineDischargeInput, control.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeToContext = new ChannelExpression<>(context.wehr4TurbineDischargeInput, control.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeToContext = new ChannelExpression<>(context.hauptkraftwerkWeirDischargeInput, control.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischargeToContext = new ChannelExpression<>(context.wehr1WeirDischargeInput, control.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischargeToContext = new ChannelExpression<>(context.wehr2WeirDischargeInput, control.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischargeToContext = new ChannelExpression<>(context.wehr3WeirDischargeInput, control.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischargeToContext = new ChannelExpression<>(context.wehr4WeirDischargeInput, control.wehr4WeirDischargeOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeToObjective = new ChannelExpression<>(objective.hauptkraftwerkTurbineDischargeInput, context.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeToObjective = new ChannelExpression<>(objective.wehr1TurbineDischargeInput, context.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeToObjective = new ChannelExpression<>(objective.wehr2TurbineDischargeInput, context.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeToObjective = new ChannelExpression<>(objective.wehr3TurbineDischargeInput, context.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeToObjective = new ChannelExpression<>(objective.wehr4TurbineDischargeInput, context.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeToObjective = new ChannelExpression<>(objective.hauptkraftwerkWeirDischargeInput, context.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischargeToObjective = new ChannelExpression<>(objective.wehr1WeirDischargeInput, context.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischargeToObjective = new ChannelExpression<>(objective.wehr2WeirDischargeInput, context.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischargeToObjective = new ChannelExpression<>(objective.wehr3WeirDischargeInput, context.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischargeToObjective = new ChannelExpression<>(objective.wehr4WeirDischargeInput, context.wehr4WeirDischargeOutput);
	
	public Expression<Double> productionToObjective = new ChannelExpression<>(objective.productionInput, context.netProductionOutput);
	public Expression<Double> priceToObjective = new ChannelExpression<>(objective.priceInput, scenario.priceOutput);

}
