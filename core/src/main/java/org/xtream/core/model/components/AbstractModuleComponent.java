package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public class AbstractModuleComponent<PhysicsComponent extends AbstractPhysicsComponent, LogicsComponent extends AbstractLogicsComponent, ConstraintsComponent extends AbstractConstraintsComponent, QualitiesComponent extends AbstractQualitiesComponent, CostsComponent extends AbstractCostsComponent, ModulesComponent extends AbstractModulesComponent> extends Component
{
	
	public AbstractModuleComponent()
	{
		
	}
	public AbstractModuleComponent(PhysicsComponent physics, LogicsComponent logics, ConstraintsComponent constraints, QualitiesComponent qualities, CostsComponent costs, ModulesComponent modules)
	{
		this.physics = physics;
		this.logics = logics;
		this.constraints = constraints;
		this.qualities = qualities;
		this.costs = costs;
		this.modules = modules;
	}
	
	// Components
	
	public PhysicsComponent physics;
	public LogicsComponent logics;
	public ConstraintsComponent constraints;
	public QualitiesComponent qualities;
	public CostsComponent costs;
	public ModulesComponent modules;

}
