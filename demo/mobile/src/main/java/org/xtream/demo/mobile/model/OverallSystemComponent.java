package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.commons.EnergyModuleComponent;
import org.xtream.demo.mobile.model.overallsystems.ConstraintsComponent;
import org.xtream.demo.mobile.model.overallsystems.CostsComponent;
import org.xtream.demo.mobile.model.overallsystems.LogicsComponent;
import org.xtream.demo.mobile.model.overallsystems.ModulesComponent;
import org.xtream.demo.mobile.model.overallsystems.PhysicsComponent;
import org.xtream.demo.mobile.model.overallsystems.QualitiesComponent;

public class OverallSystemComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{
	
	// Parameters
	
	Graph graph;
	
	private static EnergyModuleComponent<?, ?, ?, ?, ?, ?>[] getModules(int size, Graph graph)
	{
		EnergyModuleComponent<?, ?, ?, ?, ?, ?>[] modules = new EnergyModuleComponent[size];
		
		// Vehicles
		
		modules[0] = new VehicleComponent(graph, "HomeAHomeA", "WorkAWorkA", 15., 8., 3., 5.);
		modules[1] = new VehicleComponent(graph, "HomeBHomeB", "WorkBWorkB", 15., 8., 3., 5.);
		modules[2] = new VehicleComponent(graph, "HomeCHomeC", "WorkCWorkC", 15., 8., 3., 5.);
		
		return modules;
	}
	
	public OverallSystemComponent(int size, Graph graph)
	{
		this(graph, getModules(size, graph));
	}
	
	@SuppressWarnings("unchecked")
	public OverallSystemComponent(Graph graph, EnergyModuleComponent<?, ?, ?, ?, ?, ?>... modules)
	{
		super(OverallSystemComponent.class.getClassLoader().getResource("overallsystem.png"), new PhysicsComponent(modules.length), new LogicsComponent(), new ConstraintsComponent(modules.length, graph), new QualitiesComponent(), new CostsComponent(), new ModulesComponent(modules));
		
		// Balance channels	
		balances = new ChannelExpression[modules.length];

		positionTraversedLength = new ChannelExpression[modules.length];
		position = new ChannelExpression[modules.length];
		vehicleLength = new ChannelExpression[modules.length];
		
		
		for (int i = 0; i < modules.length; i++)
		{
			balances[i] = new ChannelExpression<>(physics.terminalInputs[i], this.modules.balanceOutputs[i]);

			/*
			positionTraversedLength[i] = new ChannelExpression<>(constraints.positionTraversedLengthInputs[i], this.modules.positionTraversedLengthOutputs[i]);
			position[i] = new ChannelExpression<>(constraints.positionInputs[i], this.modules.positionOutputs[i]);
			vehicleLength[i] = new ChannelExpression<>(constraints.vehicleLengthInputs[i], this.modules.vehicleLengthOutputs[i]);
			*/
		}
		
		// Previews
		
		modulePreview = new Chart(productionOutput, consumptionOutput, balanceOutput);
	}
	
	// Channels
	
	public ChannelExpression<Double> productionInternal = new ChannelExpression<>(constraints.productionInput, physics.productionOutput);
	public ChannelExpression<Double> consumptionInternal = new ChannelExpression<>(constraints.consumptionInput, physics.consumptionOutput);
	public ChannelExpression<Double> balanceInternal = new ChannelExpression<>(costs.balanceInput, physics.balanceOutput);
	public ChannelExpression<Double>[] balances;
	
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;
	

	
}
