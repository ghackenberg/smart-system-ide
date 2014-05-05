package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.mobile.model.commons.EnergyModuleComponent;
import org.xtream.demo.mobile.model.commons.VehicleEnergyModuleComponent;
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
	
	private static VehicleEnergyModuleComponent<?, ?, ?, ?, ?, ?>[] getModules(int size, Graph graph)
	{
		VehicleEnergyModuleComponent<?, ?, ?, ?, ?, ?>[] modules = new VehicleEnergyModuleComponent[size];
		
		// Vehicles
		
		/*
		modules[0] = new VehicleComponent(graph, "HomeAHomeA", "WorkAWorkA", 1., 1.);
		modules[1] = new VehicleComponent(graph, "HomeBHomeB", "WorkAWorkA", 1., 1.);
		*/
		
		// Time, Power
		modules[0] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[1] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[2] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[3] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[4] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[5] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[6] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[7] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[8] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		modules[9] = new VehicleComponent(graph, "HomeHome", "WorkWork", 1., 1.);
		
		//modules[1] = new VehicleComponent(graph, "HomeBHomeB", "WorkBWorkB", 15., 8., 3., 5.);
		//modules[2] = new VehicleComponent(graph, "HomeCHomeC", "WorkCWorkC", 15., 8., 3., 5.);
		
		return modules;
	}
	
	public OverallSystemComponent(int size, Graph graph)
	{
		this(graph, getModules(size, graph));
	}
	
	@SuppressWarnings("unchecked")
	public OverallSystemComponent(Graph graph, VehicleEnergyModuleComponent<?, ?, ?, ?, ?, ?>... vehicleEnergyModuleComponent)
	{
		super(OverallSystemComponent.class.getClassLoader().getResource("overallsystem.png"), new PhysicsComponent(vehicleEnergyModuleComponent.length), new LogicsComponent(), new ConstraintsComponent(vehicleEnergyModuleComponent.length, graph), new QualitiesComponent(), new CostsComponent(), new ModulesComponent(vehicleEnergyModuleComponent));
		
		// Balance channels	
		balances = new ChannelExpression[vehicleEnergyModuleComponent.length];

		positionTraversedLength = new ChannelExpression[vehicleEnergyModuleComponent.length];
		position = new ChannelExpression[vehicleEnergyModuleComponent.length];
		vehicleLength = new ChannelExpression[vehicleEnergyModuleComponent.length];
		
		
		for (int i = 0; i < vehicleEnergyModuleComponent.length; i++)
		{
			balances[i] = new ChannelExpression<>(physics.terminalInputs[i], this.modules.balanceOutputs[i]);

			
			positionTraversedLength[i] = new ChannelExpression<>(constraints.positionTraversedLengthInputs[i], this.modules.positionTraversedLengthOutputs[i]);
			position[i] = new ChannelExpression<>(constraints.positionInputs[i], this.modules.positionOutputs[i]);
			vehicleLength[i] = new ChannelExpression<>(constraints.vehicleLengthInputs[i], this.modules.vehicleLengthOutputs[i]);
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
