package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.commons.EnergyModuleComponent;
import org.xtream.demo.thermal.model.thermals.ConstraintsComponent;
import org.xtream.demo.thermal.model.thermals.CostsComponent;
import org.xtream.demo.thermal.model.thermals.LogicsComponent;
import org.xtream.demo.thermal.model.thermals.ModulesComponent;
import org.xtream.demo.thermal.model.thermals.PhysicsComponent;
import org.xtream.demo.thermal.model.thermals.QualitiesComponent;

public class ThermalComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{

	public ThermalComponent()
	{
		super(new PhysicsComponent(), new LogicsComponent(), new ConstraintsComponent(), new QualitiesComponent(), new CostsComponent(), new ModulesComponent());
	}
	
	// Channels
	
	public ChannelExpression<Boolean> command = new ChannelExpression<>(physics.commandInput, logics.commandOutput);
	public ChannelExpression<Double> temperature = new ChannelExpression<>(constraints.temperatureInput, physics.temperatureOutput);
	public ChannelExpression<Double> maximum = new ChannelExpression<>(constraints.maximumInput, physics.maximumOutput);
	public ChannelExpression<Double> minimum = new ChannelExpression<>(constraints.minimumInput, physics.minimumOutput);
	
	// Charts
	
	public Chart temperatureChart = new Chart(physics.temperatureOutput, physics.minimumOutput, physics.maximumOutput);
	
	// Previews
	
	public Chart temperaturePreview = new Chart(physics.temperatureOutput, physics.minimumOutput, physics.maximumOutput);

}
