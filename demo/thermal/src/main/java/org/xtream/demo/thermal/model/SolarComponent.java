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
		super(SolarComponent.class.getClassLoader().getResource("solar.gif"), new PhysicsComponent(scale), new LogicsComponent(), new ConstraintsComponent(), new QualitiesComponent(), new CostsComponent(), new ModulesComponent());
	}
	
	// Channels
	
	public ChannelExpression<Double> damping = new ChannelExpression<>(physics.damingInput, logics.dampingOutput);
	
	// Previews
	
	public Chart energyPreview = new Chart(productionOutput);

}
