package org.xtream.demo.infrastructure.model;


import java.util.HashMap;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.DynamicChannelExpression;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.model.power.PowerComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;
import org.xtream.demo.infrastructure.model.scene.SceneComponent;
import org.xtream.demo.infrastructure.model.transportation.TransportationComponent;

public class RootComponent extends Component
{
	@SuppressWarnings("unchecked")
	public RootComponent(Scenario scenario) 
	{	
		powerSystem = scenario.createPowerComponent();
		transportationSystem = scenario.createTransportationComponent(powerSystem);
		scene = scenario.createSceneComponent(transportationSystem, powerSystem);

		costInputs = new Port[2];
		costInputs[0] = new Port<>();
		costInputs[1] = new Port<>();
		
		cost = new ChannelExpression[2];	
		cost[0] = new ChannelExpression<>(costInputs[0], transportationSystem.costOutput);
		cost[1] = new ChannelExpression<>(costInputs[1], powerSystem.costOutput);
		
		balanceInputs = new Port[1];
		balanceInputs[0] = new Port<>();
		
		balance = new ChannelExpression[1];
		balance[0] = new ChannelExpression<>(balanceInputs[0], powerSystem.powerOutput);
		
		HashMap<Port<Edge>, Port<Double>> sourceRegister = new HashMap<Port<Edge>, Port<Double>>();
		HashMap<Port<Edge>, Port<Double>> targetRegister = new HashMap<Port<Edge>, Port<Double>>();
		
		for (int i = 0; i < powerSystem.chargingStations.length; i++)
		{
			sourceRegister.put(powerSystem.chargingStations[i].positionOutput, powerSystem.chargingStations[i].chargeRateOutput);
		}
		
		for (int i = 0; i < transportationSystem.cars.length; i++)
		{
			targetRegister.put(transportationSystem.cars[i].positionOutput, transportationSystem.cars[i].stateOfChargeRateOutflowOutput);
		}
		
		chargingStations = new DynamicChannelExpression[transportationSystem.cars.length];
		
		for (int i = 0; i < transportationSystem.cars.length; i++)
		{
			chargingStations[i] = new DynamicChannelExpression<>(transportationSystem.cars[i].stateOfChargeRateInflowInput, transportationSystem.cars[i].positionOutput, sourceRegister);
		}
		
		cars = new DynamicChannelExpression[powerSystem.chargingStations.length];
		
		for (int i = 0; i < powerSystem.chargingStations.length; i++)
		{
			cars[i] = new DynamicChannelExpression<>(powerSystem.chargingStations[i].chargeRateInput, powerSystem.chargingStations[i].positionOutput, targetRegister);
		}
		
		costChart = new Timeline(costOutput);
	}
	
	// Components
	
	public TransportationComponent transportationSystem;
	public PowerComponent powerSystem;
	public SceneComponent scene;
	
	// Inputs

	public Port<Double>[] costInputs;
	public Port<Double>[] balanceInputs;
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> balanceOutput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double>[] cost;
	public ChannelExpression<Double>[] balance;
	public DynamicChannelExpression<Edge, Double>[] chargingStations;
	public DynamicChannelExpression<Edge, Double>[] cars;
	
	// Objectives
	
	public Objective costObjective = new MinObjective(costOutput);

	// Expressions
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < costInputs.length; i++)
			{
				sum += costInputs[i].get(state, timepoint);
			}
			
			return ((timepoint == 0 ? 0 : costOutput.get(state, timepoint - 1)) + sum);
		}
	};
	
	public Expression<Double> balanceExpression = new Expression<Double>(balanceOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < balanceInputs.length; i++)
			{
				sum += balanceInputs[i].get(state, timepoint);
			}
			
			return sum;
		}
	};
	
	// Charts
	
	public Chart costChart;
	public Chart levelChart = new Timeline(balanceOutput);
}
