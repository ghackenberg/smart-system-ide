package org.xtream.core.model.components;

import java.net.URL;

import org.xtream.core.model.Port;
import org.xtream.core.model.aggregators.SumAggregatorComponent;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;

public abstract class AbstractModuleComponent<PhysicsComponent extends AbstractPhysicsComponent, LogicsComponent extends AbstractLogicsComponent, ConstraintsComponent extends AbstractConstraintsComponent, QualitiesComponent extends AbstractQualitiesComponent, CostsComponent extends AbstractCostsComponent, ModulesComponent extends AbstractModulesComponent> extends Component
{
	
	public AbstractModuleComponent(PhysicsComponent physics, LogicsComponent logics, ConstraintsComponent constraints, QualitiesComponent qualities, CostsComponent costs, ModulesComponent modules)
	{
		this(null, physics, logics, constraints, qualities, costs, modules);
	}
	@SuppressWarnings("unchecked")
	public AbstractModuleComponent(URL icon, PhysicsComponent physics, LogicsComponent logics, ConstraintsComponent constraints, QualitiesComponent qualities, CostsComponent costs, ModulesComponent modules)
	{
		super(icon);
		
		this.physics = physics;
		this.logics = logics;
		this.constraints = constraints;
		this.qualities = qualities;
		this.costs = costs;
		this.modules = modules;
		
		costsAggregator = new SumAggregatorComponent(modules.costsOutputs.length + 1);
		
		costsOutput = new Port<>();
		costsExternal = new ChannelExpression<>(costsOutput, costsAggregator.valueOutput);
		
		costsInternal = new ChannelExpression[modules.costsOutputs.length + 1];
		costsInternal[0] = new ChannelExpression<>(costsAggregator.itemInputs[0], costs.costsOutput);
		for (int i = 0; i < modules.costsOutputs.length; i++)
		{
			costsInternal[i + 1] = new ChannelExpression<>(costsAggregator.itemInputs[i + 1], modules.costsOutputs[i]);
		}
	}
	
	// Ports
	
	public Port<Double> costsOutput = new Port<>();
	
	// Components
	
	public PhysicsComponent physics;
	public LogicsComponent logics;
	public ConstraintsComponent constraints;
	public QualitiesComponent qualities;
	public CostsComponent costs;
	public ModulesComponent modules;
	public SumAggregatorComponent costsAggregator;
	
	// Channels
	
	public ChannelExpression<Double>[] costsInternal;
	public ChannelExpression<Double> costsExternal;
	
	// Previews
	
	public Timeline modulePreview;

}
