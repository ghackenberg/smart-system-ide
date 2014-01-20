package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.commons.EnergyModuleComponent;
import org.xtream.demo.thermal.model.storages.ConstraintsComponent;
import org.xtream.demo.thermal.model.storages.CostsComponent;
import org.xtream.demo.thermal.model.storages.LogicsComponent;
import org.xtream.demo.thermal.model.storages.ModulesComponent;
import org.xtream.demo.thermal.model.storages.PhysicsComponent;
import org.xtream.demo.thermal.model.storages.QualitiesComponent;

public class StorageComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{

	public StorageComponent(double speed, double capacity)
	{
		super(StorageComponent.class.getClassLoader().getResource("buffer.png"), new PhysicsComponent(speed, capacity), new LogicsComponent(), new ConstraintsComponent(), new QualitiesComponent(), new CostsComponent(), new ModulesComponent());
		
		// Previews
		
		modulePreview = new Chart(physics.levelOutput, physics.minimumOutput, physics.maximumOutput);
	}
	
	// Channels

	public ChannelExpression<Double> command = new ChannelExpression<>(physics.commandInput, logics.commandOutput);
	public ChannelExpression<Double> level = new ChannelExpression<>(constraints.levelInput, physics.levelOutput);
	public ChannelExpression<Double> maximum = new ChannelExpression<>(constraints.maximumInput, physics.maximumOutput);
	public ChannelExpression<Double> minimum = new ChannelExpression<>(constraints.minimumInput, physics.minimumOutput);
	
	// Charts
	
	public Chart levelChart = new Chart(physics.levelOutput, physics.minimumOutput, physics.maximumOutput);

}
