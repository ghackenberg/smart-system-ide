package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Histogram;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.commons.EnergyModuleComponent;
import org.xtream.demo.mobile.model.vehicles.ConstraintsComponent;
import org.xtream.demo.mobile.model.vehicles.CostsComponent;
import org.xtream.demo.mobile.model.vehicles.LogicsComponent;
import org.xtream.demo.mobile.model.vehicles.ModulesComponent;
import org.xtream.demo.mobile.model.vehicles.PhysicsComponent;
import org.xtream.demo.mobile.model.vehicles.QualitiesComponent;

public class VehicleComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{

	public VehicleComponent(Graph graph, String startPosition, String destinationPosition, Double positionWeight, Double timeWeight, Double powerWeight, Double chargeStateWeight)
	{
		super(VehicleComponent.class.getClassLoader().getResource("vehicle.png"), new PhysicsComponent(graph, startPosition, destinationPosition), new LogicsComponent(), new ConstraintsComponent(), new QualitiesComponent(graph), new CostsComponent(positionWeight, timeWeight, powerWeight, chargeStateWeight), new ModulesComponent());
		
		// Previews
		
		modulePreview = new Chart(physics.powerOutput);
	}
	
	// Ports

	public Port<Double> speedOutput = new Port<>();
	public Port<Double> positionTraversedLengthOutput = new Port<>();
	public Port<Edge> positionOutput = new Port<>();
	public Port<Double> vehicleLengthOutput = new Port<>();
	
	// Equivalences
	
	public Equivalence speedEquivalence = new Equivalence(speedOutput);
	
	// Channels
	
	// Channels logics -> overallsystem
	
	public ChannelExpression<Double> speed = new ChannelExpression<>(speedOutput, logics.speedOutput);
	public ChannelExpression<Double> positionTraversedLength = new ChannelExpression<>(positionTraversedLengthOutput, physics.positionTraversedLengthOutput);
	public ChannelExpression<Edge> position3 = new ChannelExpression<>(positionOutput, logics.positionOutput);
	public ChannelExpression<Double> vehicleLength = new ChannelExpression<>(vehicleLengthOutput, physics.vehicleLengthOutput);
	
	// Channels physics -> logics
	
	public ChannelExpression<Edge> startPosition = new ChannelExpression<>(logics.startPositionInput, physics.startPositionOutput);
	public ChannelExpression<Edge> destinationPosition = new ChannelExpression<>(logics.destinationPositionInput, physics.destinationPositionOutput);
	public ChannelExpression<Edge> positionOutgoingEdges = new ChannelExpression<>(logics.positionOutgoingEdgesInput, physics.positionOutgoingEdgesOutput);
	public ChannelExpression<Double> positionTraversedLength2 = new ChannelExpression<>(logics.positionTraversedLengthInput, physics.positionTraversedLengthOutput);
	public ChannelExpression<Double> positionEdgeLength = new ChannelExpression<>(logics.positionEdgeLengthInput, physics.positionEdgeLengthOutput);
	
	public ChannelExpression<Boolean> drivingIndicator = new ChannelExpression<>(logics.drivingIndicatorInput, physics.drivingIndicatorOutput);
	
	// Channels logics -> physics
	
	public ChannelExpression<Edge> position = new ChannelExpression<>(physics.positionInput, logics.positionOutput);
	public ChannelExpression<Double> speedInternal = new ChannelExpression<>(physics.speedInput, logics.speedOutput);
	
	// Channels logics -> qualities
	
	public ChannelExpression<Edge> position2 = new ChannelExpression<>(qualities.positionInput, logics.positionOutput);
	public ChannelExpression<Edge> positionTarget = new ChannelExpression<>(qualities.positionTargetInput, logics.positionTargetOutput);
	
	// Channels qualities -> costs
	
	public ChannelExpression<Double> positionCosts = new ChannelExpression<>(costs.positionCostsInput, qualities.positionCostsOutput);
	public ChannelExpression<Double> timeCosts = new ChannelExpression<>(costs.timeCostsInput, qualities.timeCostsOutput);
	public ChannelExpression<Double> powerCosts = new ChannelExpression<>(costs.powerCostsInput, qualities.powerCostsOutput);
	public ChannelExpression<Double> chargeStateCosts = new ChannelExpression<>(costs.chargeStateCostsInput, qualities.chargeStateCostsOutput);
	
	// Channels: physics -> qualities
	
	public ChannelExpression<Double> power = new ChannelExpression<>(qualities.powerInput, physics.powerOutput);
	public ChannelExpression<Double> chargeState = new ChannelExpression<>(qualities.chargeStateInput, physics.chargeStateOutput);
	public ChannelExpression<Double> maximumChargeState2 = new ChannelExpression<>(qualities.maximumChargeStateInput, physics.maximumChargeStateOutput);
	public ChannelExpression<Boolean> targetReached = new ChannelExpression<>(qualities.targetReachedInput, physics.targetReachedOutput);
	public ChannelExpression<Boolean> drivingIndicator2 = new ChannelExpression<>(qualities.drivingIndicatorInput, physics.drivingIndicatorOutput);
	public ChannelExpression<Edge> destinationPosition2 = new ChannelExpression<>(qualities.destinationPositionInput, physics.destinationPositionOutput);

	// Channels physics -> constraints
	
	public ChannelExpression<Double> maximumChargeState = new ChannelExpression<>(constraints.maximumChargeStateInput, physics.maximumChargeStateOutput);
	public ChannelExpression<Double> minimumChargeState = new ChannelExpression<>(constraints.minimumChargeStateInput, physics.minimumChargeStateOutput);
	public ChannelExpression<Double> chargeState2 = new ChannelExpression<>(constraints.chargeStateInput, physics.chargeStateOutput);
	
	// Charts

	public Chart chargeStateChart = new Chart(physics.chargeStateOutput, physics.minimumChargeStateOutput, physics.maximumChargeStateOutput);
	public Chart powerChart = new Chart(physics.powerOutput);
	public Chart speedChart = new Chart(logics.speedOutput);
	
	// Histograms

	public Histogram positionHistogram = new Histogram(logics.positionOutput);

}
