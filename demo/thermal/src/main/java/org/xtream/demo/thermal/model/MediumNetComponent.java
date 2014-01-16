package org.xtream.demo.thermal.model;

import org.xtream.demo.thermal.model.nets.medium.ModulesComponent;

public class MediumNetComponent extends NetComponent<ModulesComponent>
{
	
	public MediumNetComponent(LowNetComponent... modules)
	{
		super(modules.length, new ModulesComponent(modules));
	}

}
