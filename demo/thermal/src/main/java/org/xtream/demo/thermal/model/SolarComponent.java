package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.commons.EnergyModuleComponent;
import org.xtream.demo.thermal.model.solars.ConstraintsComponent;
import org.xtream.demo.thermal.model.solars.CostsComponent;
import org.xtream.demo.thermal.model.solars.LogicsComponent;
import org.xtream.demo.thermal.model.solars.ModulesComponent;
import org.xtream.demo.thermal.model.solars.PhysicsComponent;
import org.xtream.demo.thermal.model.solars.QualitiesComponent;

public class SolarComponent extends EnergyModuleComponent<PhysicsComponent, LogicsComponent, ConstraintsComponent, QualitiesComponent, CostsComponent, ModulesComponent>
{
	
	public SolarComponent(double scale)
	{
		super(SolarComponent.class.getClassLoader().getResource("producer.png"), new PhysicsComponent(scale), new LogicsComponent(), new ConstraintsComponent(), new QualitiesComponent(), new CostsComponent(), new ModulesComponent());
		
		// Previews
		
		modulePreview = new Chart(productionOutput);
	}
	
	// Channels
	
	public ChannelExpression<Double> dampingPhysics = new ChannelExpression<>(physics.damingInput, logics.dampingOutput);
	public ChannelExpression<Double> dampingCosts = new ChannelExpression<>(costs.dampingInput, logics.dampingOutput);

}
