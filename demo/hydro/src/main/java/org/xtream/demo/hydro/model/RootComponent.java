package org.xtream.demo.hydro.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.charts.Series;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.BackgroundComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.PlaneComponent;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.optimizer.beam.Strategy;
import org.xtream.core.optimizer.beam.strategies.GridStrategy;
import org.xtream.core.optimizer.beam.strategies.KMeansStrategy;
import org.xtream.core.optimizer.beam.strategies.RandomStrategy;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	// Main
	
	public static void main(String[] args)
	{
		Strategy strategy = null;
		
		if (Constants.STRATEGY == 0)
		{
			strategy = new KMeansStrategy(Constants.CLUSTER_ROUNDS, Constants.CLUSTER_DURATION);
		}
		else if (Constants.STRATEGY == 1)
		{
			strategy = new GridStrategy();
		}
		else if (Constants.STRATEGY == 2)
		{
			strategy = new RandomStrategy();
		}
		else
		{
			throw new IllegalStateException("Strategy not defined (" + Constants.STRATEGY + ")!");
		}
		
		Engine<RootComponent> engine = new Engine<>(new RootComponent(), Constants.SAMPLES, Constants.CLUSTERS, Constants.BRANCH_ROUNDS, Constants.BRANCH_DURATION, Constants.PRUNE, strategy);
		
		new Workbench<>(engine, Constants.DURATION);
	}
	
	// Components
	
	// Reactive Components

	public ScenarioComponent scenario = new ScenarioComponent();
	public ControlComponent control_reactive = Constants.PRUNE ? new org.xtream.demo.hydro.model.control.reactive.single.discrete.ControlComponent() : new org.xtream.demo.hydro.model.control.actual.ControlComponent();
	//public ControlComponent control_reactive = new org.xtream.demo.hydro.model.control.reactive.single.continuous.ControlComponent();
	//public ControlComponent control_reactive = new org.xtream.demo.hydro.model.control.reactive.single.discrete.ControlComponent();
	//public ControlComponent control_reactive = new org.xtream.demo.hydro.model.control.reactive.split.continuous.ControlComponent();
	//public ControlComponent control_reactive = new org.xtream.demo.hydro.model.control.reactive.split.discrete.ControlComponent();
	public org.xtream.demo.hydro.model.context.reactive.ContextComponent context_reactive = new org.xtream.demo.hydro.model.context.reactive.ContextComponent();
	public ObjectiveComponent objective_reactive = new ObjectiveComponent();
	public EquivalenceComponent equivalence_reactive = new EquivalenceComponent();
	
	// Actual Components

	public org.xtream.demo.hydro.model.context.actual.ContextComponent context_actual = new org.xtream.demo.hydro.model.context.actual.ContextComponent();
	public ObjectiveComponent objective_actual = new ObjectiveComponent();
	
	// Scene Components
	
	public IdentityComponent identity = new IdentityComponent()
	{
		// empty
	};
	public BackgroundComponent background = new BackgroundComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255,255,255));
	};
	public AmbientComponent ambient = new AmbientComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(128,128,128));
	};
	public CameraComponent camera = new CameraComponent()
	{
		@SuppressWarnings("unused")
		public Expression<RealVector> eyeExpression = new ConstantExpression<RealVector>(eyeOutput, new ArrayRealVector(new double[] {10.,5.,4.5,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> centerExpression = new ConstantExpression<RealVector>(centerOutput, new ArrayRealVector(new double[] {0,0,4.5,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> upExpression = new ConstantExpression<RealVector>(upOutput, new ArrayRealVector(new double[] {0,1,0,1}));
	};
	public PointLightComponent light = new PointLightComponent()
	{
		@SuppressWarnings("unused")
		public Expression<RealVector> positionExpression = new ConstantExpression<RealVector>(positionOutput, new ArrayRealVector(new double[] {5,10,10,1}));
		@SuppressWarnings("unused")
		public Expression<Color> specularExpression = new ConstantExpression<Color>(specularOutput, new Color(255,255,255));
		@SuppressWarnings("unused")
		public Expression<Color> diffuseExpression = new ConstantExpression<Color>(diffuseOutput, new Color(255,255,255));
	};
	public PlaneComponent plane = new PlaneComponent() 
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
		@SuppressWarnings("unused")
		public Expression<Double> heightExpression = new ConstantExpression<Double>(heightOutput, 0.);	
	};
	
	// Objectives
	
	public Objective objectiveMarker = new MaxObjective(objective_reactive.objectiveOutput);
	
	// Charts

	public Chart inflowChart = new Timeline("Zufluss Bigonville", "Viertelstunde", "Kubikmeter pro Sekunde", new Series<>("Zufluss Bigonville", scenario.inflowOutput));
	public Chart priceChart = new Timeline("Energiepreis", "Viertelstunde", "Euro", new Series<>("Energiepreis", scenario.priceOutput));
	public Chart objectiveChart = new Timeline("Zielfunktion", "Viertelstunde", "Euro", new Series<>("Umsatz geschätzt", objective_reactive.rewardOutput), new Series<>("Kosten geschätzt", objective_reactive.costOutput), new Series<>("Gewinn geschätzt", objective_reactive.objectiveOutput), new Series<>("Umsatz gemessen", objective_actual.rewardOutput), new Series<>("Kosten gemessen", objective_actual.costOutput), new Series<>("Gewinn gemessen", objective_actual.objectiveOutput));

	public Chart productionChart = new Timeline("Vergleich Energieproduktion", "Viertelstunde", "Kilowatt", new Series<>("Produktion geschätzt", context_reactive.netProductionOutput), new Series<>("Produktion gemessen", context_actual.netProductionOutput));
	public Chart speicherseeLevelChart = new Timeline("Vergleich Speicherseepegel", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.speicherseeLevelOutput), new Series<>("Pegel gemessen", context_actual.speicherseeLevelOutput));
	public Chart volumen1LevelChart = new Timeline("Vergleich Segmentpegel 1", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.volumen1LevelOutput), new Series<>("Pegel gemessen", context_actual.volumen1LevelOutput));
	public Chart volumen2LevelChart = new Timeline("Vergleich Segmentpegel 2", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.volumen2LevelOutput), new Series<>("Pegel gemessen", context_actual.volumen2LevelOutput));
	public Chart volumen3LevelChart = new Timeline("Vergleich Segmentpegel 3", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.volumen3LevelOutput), new Series<>("Pegel gemessen", context_actual.volumen3LevelOutput));
	public Chart volumen4LevelChart = new Timeline("Vergleich Segmentpegel 4", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.volumen4LevelOutput), new Series<>("Pegel gemessen", context_actual.volumen4LevelOutput));
	public Chart volumen5LevelChart = new Timeline("Vergleich Heidescheider Grundpegel", "Viertelstunde", "Meter", new Series<>("Pegel geschätzt", context_reactive.volumen5LevelOutput), new Series<>("Pegel gemessen", context_actual.volumen5LevelOutput));
	
	// Expressions
 
	public Expression<Double> inflowToContext = new ChannelExpression<>(context_reactive.inflowInput, scenario.inflowOutput);
	
	public Expression<Double> inflowToControl = new ChannelExpression<>(control_reactive.inflowInput, scenario.inflowOutput);
	public Expression<Double> priceToControl = new ChannelExpression<>(control_reactive.priceInput, scenario.priceOutput);
	
	public Expression<Double> speicherseeLevelToControl = new ChannelExpression<>(control_reactive.speicherseeLevelInput, context_reactive.speicherseeLevelOutput);
	public Expression<Double> volumen1LevelToControl = new ChannelExpression<>(control_reactive.volumen1LevelInput, context_reactive.volumen1LevelOutput);
	public Expression<Double> volumen2LevelToControl = new ChannelExpression<>(control_reactive.volumen2LevelInput, context_reactive.volumen2LevelOutput);
	public Expression<Double> volumen3LevelToControl = new ChannelExpression<>(control_reactive.volumen3LevelInput, context_reactive.volumen3LevelOutput);
	public Expression<Double> volumen4LevelToControl = new ChannelExpression<>(control_reactive.volumen4LevelInput, context_reactive.volumen4LevelOutput);
	
	public Expression<Double> speicherseeLevelToEquivalence = new ChannelExpression<>(equivalence_reactive.speicherseeLevelInput, context_reactive.speicherseeLevelOutput);
	public Expression<Double> volumen1LevelToEquivalence = new ChannelExpression<>(equivalence_reactive.volumen1LevelInput, context_reactive.volumen1LevelOutput);
	public Expression<Double> volumen2LevelToEquivalence = new ChannelExpression<>(equivalence_reactive.volumen2LevelInput, context_reactive.volumen2LevelOutput);
	public Expression<Double> volumen3LevelToEquivalence = new ChannelExpression<>(equivalence_reactive.volumen3LevelInput, context_reactive.volumen3LevelOutput);
	public Expression<Double> volumen4LevelToEquivalence = new ChannelExpression<>(equivalence_reactive.volumen4LevelInput, context_reactive.volumen4LevelOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeToContext = new ChannelExpression<>(context_reactive.hauptkraftwerkTurbineDischargeInput, control_reactive.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeToContext = new ChannelExpression<>(context_reactive.wehr1TurbineDischargeInput, control_reactive.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeToContext = new ChannelExpression<>(context_reactive.wehr2TurbineDischargeInput, control_reactive.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeToContext = new ChannelExpression<>(context_reactive.wehr3TurbineDischargeInput, control_reactive.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeToContext = new ChannelExpression<>(context_reactive.wehr4TurbineDischargeInput, control_reactive.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeToContext = new ChannelExpression<>(context_reactive.hauptkraftwerkWeirDischargeInput, control_reactive.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischargeToContext = new ChannelExpression<>(context_reactive.wehr1WeirDischargeInput, control_reactive.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischargeToContext = new ChannelExpression<>(context_reactive.wehr2WeirDischargeInput, control_reactive.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischargeToContext = new ChannelExpression<>(context_reactive.wehr3WeirDischargeInput, control_reactive.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischargeToContext = new ChannelExpression<>(context_reactive.wehr4WeirDischargeInput, control_reactive.wehr4WeirDischargeOutput);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeToObjective = new ChannelExpression<>(objective_reactive.hauptkraftwerkTurbineDischargeInput, context_reactive.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr1TurbineDischargeInput, context_reactive.wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr2TurbineDischargeInput, context_reactive.wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr3TurbineDischargeInput, context_reactive.wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr4TurbineDischargeInput, context_reactive.wehr4TurbineDischargeOutput);
	
	public Expression<Double> hauptkraftwerkWeirDischargeToObjective = new ChannelExpression<>(objective_reactive.hauptkraftwerkWeirDischargeInput, context_reactive.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> wehr1WeirDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr1WeirDischargeInput, context_reactive.wehr1WeirDischargeOutput);
	public Expression<Double> wehr2WeirDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr2WeirDischargeInput, context_reactive.wehr2WeirDischargeOutput);
	public Expression<Double> wehr3WeirDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr3WeirDischargeInput, context_reactive.wehr3WeirDischargeOutput);
	public Expression<Double> wehr4WeirDischargeToObjective = new ChannelExpression<>(objective_reactive.wehr4WeirDischargeInput, context_reactive.wehr4WeirDischargeOutput);
	
	public Expression<Double> productionToObjective = new ChannelExpression<>(objective_reactive.productionInput, context_reactive.netProductionOutput);
	public Expression<Double> priceToObjective = new ChannelExpression<>(objective_reactive.priceInput, scenario.priceOutput);
	
	public Expression<Double> actualHauptkraftwerkTurbineDischargeToObjective = new ChannelExpression<>(objective_actual.hauptkraftwerkTurbineDischargeInput, context_actual.hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> actualWehr1TurbineDischargeToObjective = new ChannelExpression<>(objective_actual.wehr1TurbineDischargeInput, context_actual.wehr1TurbineDischargeOutput);
	public Expression<Double> actualWehr2TurbineDischargeToObjective = new ChannelExpression<>(objective_actual.wehr2TurbineDischargeInput, context_actual.wehr2TurbineDischargeOutput);
	public Expression<Double> actualWehr3TurbineDischargeToObjective = new ChannelExpression<>(objective_actual.wehr3TurbineDischargeInput, context_actual.wehr3TurbineDischargeOutput);
	public Expression<Double> actualWehr4TurbineDischargeToObjective = new ChannelExpression<>(objective_actual.wehr4TurbineDischargeInput, context_actual.wehr4TurbineDischargeOutput);
	
	public Expression<Double> actualHauptkraftwerkWeirDischargeToObjective = new ChannelExpression<>(objective_actual.hauptkraftwerkWeirDischargeInput, context_actual.hauptkraftwerkWeirDischargeOutput);
	public Expression<Double> actualWehr1WeirDischargeToObjective = new ChannelExpression<>(objective_actual.wehr1WeirDischargeInput, context_actual.wehr1WeirDischargeOutput);
	public Expression<Double> actualWehr2WeirDischargeToObjective = new ChannelExpression<>(objective_actual.wehr2WeirDischargeInput, context_actual.wehr2WeirDischargeOutput);
	public Expression<Double> actualWehr3WeirDischargeToObjective = new ChannelExpression<>(objective_actual.wehr3WeirDischargeInput, context_actual.wehr3WeirDischargeOutput);
	public Expression<Double> actualWehr4WeirDischargeToObjective = new ChannelExpression<>(objective_actual.wehr4WeirDischargeInput, context_actual.wehr4WeirDischargeOutput);
	
	public Expression<Double> actualProductionToObjective = new ChannelExpression<>(objective_actual.productionInput, context_actual.netProductionOutput);
	public Expression<Double> actualPriceToObjective = new ChannelExpression<>(objective_actual.priceInput, scenario.priceOutput);
	
	public ChannelExpression<RealMatrix> identityToCamera = new ChannelExpression<RealMatrix>(camera.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToLight = new ChannelExpression<RealMatrix>(light.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToPlane = new ChannelExpression<RealMatrix>(plane.transformInput, identity.transformOutput);
	
}
