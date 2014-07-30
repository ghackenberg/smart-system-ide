package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.vehicle.ConstraintsComponent;
import org.xtream.demo.mobile.model.vehicle.ContextComponent;
import org.xtream.demo.mobile.model.vehicle.ObjectiveComponent;
import org.xtream.demo.mobile.model.vehicle.LogicsComponent;

public class VehicleContainer extends GenericModuleContainer
{

    // Components
    public ContextComponent context;
    public LogicsComponent logics;
    public ConstraintsComponent constraints;
    public ObjectiveComponent objectives;

    // Previews
    public Chart modulePreview;

	public VehicleContainer(Graph graph, String startPosition, String destinationPosition, Double timeWeight, Double powerWeight)
	{
        this.context = new ContextComponent(graph, startPosition, destinationPosition);
        this.logics = new LogicsComponent(graph);
        this.constraints = new ConstraintsComponent();
        this.objectives = new ObjectiveComponent(graph, timeWeight, powerWeight);

        chargeStateRelative = new ChannelExpression<>(chargeStateRelativeOutput, context.chargeStateRelativeOutput);
        speed = new ChannelExpression<>(speedOutput, logics.speedOutput);
        positionTraversedLength = new ChannelExpression<>(positionTraversedLengthOutput, context.positionTraversedLengthOutput);
        position = new ChannelExpression<>(positionOutput, logics.positionOutput);
        vehicleLength = new ChannelExpression<>(vehicleLengthOutput, context.vehicleLengthOutput);
        timeCosts = new ChannelExpression<>(timeCostOutput, objectives.timeCostsOutput);
        powerCosts = new ChannelExpression<>(powerCostOutput, objectives.powerCostsOutput);
        this.startPosition = new ChannelExpression<>(startPositionOutput, context.startPositionOutput);
        this.destinationPosition = new ChannelExpression<>(destinationPositionOutput, context.destinationPositionOutput);
        power = new ChannelExpression<>(powerOutput, context.powerOutput);
        startPosition2 = new ChannelExpression<>(logics.startPositionInput, context.startPositionOutput);
        destinationPosition2 = new ChannelExpression<>(logics.destinationPositionInput, context.destinationPositionOutput);
        positionOutgoingEdges = new ChannelExpression<>(logics.positionOutgoingEdgesInput, context.positionOutgoingEdgesOutput);
        positionTraversedLength2 = new ChannelExpression<>(logics.positionTraversedLengthInput, context.positionTraversedLengthOutput);
        positionEdgeLength = new ChannelExpression<>(logics.positionEdgeLengthInput, context.positionEdgeLengthOutput);
        drivingIndicator2 = new ChannelExpression<>(logics.drivingIndicatorInput, context.drivingIndicatorOutput);
        position2 = new ChannelExpression<>(context.positionInput, logics.positionOutput);
        speedInternal = new ChannelExpression<>(context.speedInput, logics.speedOutput);
        timeCosts2 = new ChannelExpression<>(objectives.timeCostsInput, objectives.timeCostsOutput);
        powerCosts2 = new ChannelExpression<>(objectives.powerCostsInput, objectives.powerCostsOutput);
        power2 = new ChannelExpression<>(objectives.powerInput, context.powerOutput);
        targetReached = new ChannelExpression<>(objectives.targetReachedInput, context.targetReachedOutput);
        drivingIndicator = new ChannelExpression<>(objectives.drivingIndicatorInput, context.drivingIndicatorOutput);
        maximumChargeState = new ChannelExpression<>(constraints.maximumChargeStateInput, context.maximumChargeStateOutput);
        minimumChargeState = new ChannelExpression<>(constraints.minimumChargeStateInput, context.minimumChargeStateOutput);
        chargeState2 = new ChannelExpression<>(constraints.chargeStateInput, context.chargeStateOutput);
        
        // Charts
        chargeStateChart = new Timeline(context.chargeStateOutput, context.minimumChargeStateOutput, context.maximumChargeStateOutput);
        powerChart = new Timeline(context.powerOutput);
        powerAggregateChart = new Timeline(powerAggregateOutput);
        speedChart = new Timeline(logics.speedOutput);
        speedAbsoluteChart = new Timeline(logics.speedAbsoluteOutput);
        AggregateChart = new Timeline(speedAggregateOutput, powerAggregateOutput);
        costChart = new Timeline(objectives.costsOutput);
        costSumChart = new Timeline(costOutput);
        positionHistogram = new Histogram<>(logics.positionOutput);
        
		// Previews
		
		modulePreview = new Timeline(context.powerOutput);
	}

	// Ports

	public Port<Double> chargeStateRelativeOutput = new Port<>();
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
    public Port<Double> balanceOutput = new Port<>();
	
	
	// Channels
	
	// Channels logics -> net
	
    public ChannelExpression<Double> chargeStateRelative;
	public ChannelExpression<Double> speed;
	public ChannelExpression<Double> positionTraversedLength;
	public ChannelExpression<Edge> position;
	public ChannelExpression<Double> vehicleLength;
	
	public ChannelExpression<Double> timeCosts;
	public ChannelExpression<Double> powerCosts;
	
	public ChannelExpression<Edge> startPosition;
	public ChannelExpression<Edge> destinationPosition;
	
	// Channels physics -> net
	
	public ChannelExpression<Double> power;
	
	// Channels physics -> logics
	
	public ChannelExpression<Edge> startPosition2;
	public ChannelExpression<Edge> destinationPosition2;
	public ChannelExpression<Edge> positionOutgoingEdges;
	public ChannelExpression<Double> positionTraversedLength2;
	public ChannelExpression<Double> positionEdgeLength;
	public ChannelExpression<Boolean> drivingIndicator2;
	
	// Channels logics -> physics
	
	public ChannelExpression<Edge> position2;
	public ChannelExpression<Double> speedInternal;
	
	// Channels qualities -> costs
	
	public ChannelExpression<Double> timeCosts2;
	public ChannelExpression<Double> powerCosts2;
	
	// Channels: physics -> qualities
	
	public ChannelExpression<Double> power2;
	public ChannelExpression<Boolean> targetReached;
	public ChannelExpression<Boolean> drivingIndicator;

	// Channels physics -> constraints
	
	public ChannelExpression<Double> maximumChargeState;
	public ChannelExpression<Double> minimumChargeState;
	public ChannelExpression<Double> chargeState2;
	
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : costOutput.get(state, timepoint - 1)) + objectives.costsOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> speedAggregateExpression = new Expression<Double>(speedAggregateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : speedAggregateOutput.get(state, timepoint - 1)) + logics.speedOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> powerAggregateExpression = new Expression<Double>(powerAggregateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : powerAggregateOutput.get(state, timepoint - 1)) + context.powerOutput.get(state, timepoint);
		}
	};
	
	// Charts

	public Chart chargeStateChart;
	public Chart powerChart;
	public Chart powerAggregateChart;
	public Chart speedChart;
	public Chart speedAbsoluteChart;
	public Chart AggregateChart;
	public Chart costChart;
	public Chart costSumChart;

	public Chart positionHistogram;

}
