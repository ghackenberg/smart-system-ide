package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.commons.VehicleEnergyModuleComponent;
import org.xtream.demo.mobile.model.vehicles.ConstraintsComponent;
import org.xtream.demo.mobile.model.vehicles.CostsComponent;
import org.xtream.demo.mobile.model.vehicles.LogicsComponent;
import org.xtream.demo.mobile.model.vehicles.ModulesComponent;
import org.xtream.demo.mobile.model.vehicles.PhysicsComponent;
import org.xtream.demo.mobile.model.vehicles.QualitiesComponent;

public class VehicleComponent extends VehicleEnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{

	public VehicleComponent(Graph graph, String startPosition, String destinationPosition, Double timeWeight, Double powerWeight)
	{
		super(VehicleComponent.class.getClassLoader().getResource("vehicle.png"), new PhysicsComponent(graph, startPosition, destinationPosition), new LogicsComponent(graph), new ConstraintsComponent(), new QualitiesComponent(), new CostsComponent(timeWeight, powerWeight), new ModulesComponent(), graph);
		
		// Previews
		
		modulePreview = new Timeline(physics.powerOutput);
	}
	
	// Ports

	public Port<Double> powerOutput = new Port<>();
	public Port<Double> speedOutput = new Port<>();
	public Port<Double> positionTraversedLengthOutput = new Port<>();
	public Port<Edge> positionOutput = new Port<>();
	public Port<Double> vehicleLengthOutput = new Port<>();
	public Port<Double> costOutput = new Port<>();
	public Port<Double> speedAggregateOutput = new Port<>();
	public Port<Double> powerAggregateOutput = new Port<>();
	public Port<Double> timeCostOutput = new Port<>();
	public Port<Double> powerCostOutput = new Port<>();
	public Port<Edge> startPositionOutput = new Port<>();
	public Port<Edge> destinationPositionOutput = new Port<>();
	
	
	// Channels
	
	// Channels logics -> overallsystem
	
	public ChannelExpression<Double> speed = new ChannelExpression<>(speedOutput, logics.speedOutput);
	public ChannelExpression<Double> positionTraversedLength = new ChannelExpression<>(positionTraversedLengthOutput, physics.positionTraversedLengthOutput);
	public ChannelExpression<Edge> position = new ChannelExpression<>(positionOutput, logics.positionOutput);
	public ChannelExpression<Double> vehicleLength = new ChannelExpression<>(vehicleLengthOutput, physics.vehicleLengthOutput);
	
	public ChannelExpression<Double> timeCosts = new ChannelExpression<>(timeCostOutput, qualities.timeCostsOutput);
	public ChannelExpression<Double> powerCosts = new ChannelExpression<>(powerCostOutput, qualities.powerCostsOutput);
	
	public ChannelExpression<Edge> startPosition = new ChannelExpression<>(startPositionOutput, physics.startPositionOutput);
	public ChannelExpression<Edge> destinationPosition = new ChannelExpression<>(destinationPositionOutput, physics.destinationPositionOutput);
	
	// Channels physics -> overallsystem
	public ChannelExpression<Double> power = new ChannelExpression<>(powerOutput, physics.powerOutput);
	
	// Channels physics -> logics
	
	public ChannelExpression<Edge> startPosition2 = new ChannelExpression<>(logics.startPositionInput, physics.startPositionOutput);
	public ChannelExpression<Edge> destinationPosition2 = new ChannelExpression<>(logics.destinationPositionInput, physics.destinationPositionOutput);
	public ChannelExpression<Edge> positionOutgoingEdges = new ChannelExpression<>(logics.positionOutgoingEdgesInput, physics.positionOutgoingEdgesOutput);
	public ChannelExpression<Double> positionTraversedLength2 = new ChannelExpression<>(logics.positionTraversedLengthInput, physics.positionTraversedLengthOutput);
	public ChannelExpression<Double> positionEdgeLength = new ChannelExpression<>(logics.positionEdgeLengthInput, physics.positionEdgeLengthOutput);
	public ChannelExpression<Boolean> drivingIndicator2 = new ChannelExpression<>(logics.drivingIndicatorInput, physics.drivingIndicatorOutput);
	
	// Channels logics -> physics
	
	public ChannelExpression<Edge> position2 = new ChannelExpression<>(physics.positionInput, logics.positionOutput);
	public ChannelExpression<Double> speedInternal = new ChannelExpression<>(physics.speedInput, logics.speedOutput);
	
	// Channels qualities -> costs
	
	public ChannelExpression<Double> timeCosts2 = new ChannelExpression<>(costs.timeCostsInput, qualities.timeCostsOutput);
	public ChannelExpression<Double> powerCosts2 = new ChannelExpression<>(costs.powerCostsInput, qualities.powerCostsOutput);
	
	// Channels: physics -> qualities
	
	public ChannelExpression<Double> power2 = new ChannelExpression<>(qualities.powerInput, physics.powerOutput);
	public ChannelExpression<Boolean> targetReached = new ChannelExpression<>(qualities.targetReachedInput, physics.targetReachedOutput);
	public ChannelExpression<Boolean> drivingIndicator = new ChannelExpression<>(qualities.drivingIndicatorInput, physics.drivingIndicatorOutput);

	// Channels physics -> constraints
	
	public ChannelExpression<Double> maximumChargeState = new ChannelExpression<>(constraints.maximumChargeStateInput, physics.maximumChargeStateOutput);
	public ChannelExpression<Double> minimumChargeState = new ChannelExpression<>(constraints.minimumChargeStateInput, physics.minimumChargeStateOutput);
	public ChannelExpression<Double> chargeState2 = new ChannelExpression<>(constraints.chargeStateInput, physics.chargeStateOutput);
	
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (timepoint == 0 ? 0 : costOutput.get(timepoint - 1)) + costs.costsOutput.get(timepoint);
		}
	};
	
	public Expression<Double> speedAggregateExpression = new Expression<Double>(speedAggregateOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (timepoint == 0 ? 0 : speedAggregateOutput.get(timepoint - 1)) + logics.speedOutput.get(timepoint);
		}
	};
	
	public Expression<Double> powerAggregateExpression = new Expression<Double>(powerAggregateOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (timepoint == 0 ? 0 : powerAggregateOutput.get(timepoint - 1)) + physics.powerOutput.get(timepoint);
		}
	};
	
	// Charts

	public Chart chargeStateChart = new Timeline(physics.chargeStateOutput, physics.minimumChargeStateOutput, physics.maximumChargeStateOutput);
	public Chart powerChart = new Timeline(physics.powerOutput);
	public Chart powerAggregateChart = new Timeline(powerAggregateOutput);
	public Chart speedChart = new Timeline(logics.speedOutput);
	public Chart speedAbsoluteChart = new Timeline(logics.speedAbsoluteOutput);
	public Chart AggregateChart = new Timeline(speedAggregateOutput, powerAggregateOutput);
	public Chart costChart = new Timeline(costs.costsOutput);
	public Chart costSumChart = new Timeline(costOutput);

	public Chart positionHistogram = new Histogram<>(logics.positionOutput);

}
