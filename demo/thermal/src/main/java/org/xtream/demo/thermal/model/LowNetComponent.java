package org.xtream.demo.thermal.model;

import org.xtream.demo.thermal.model.nets.low.ModulesComponent;

public class LowNetComponent extends NetComponent<ModulesComponent>
{
	
	public LowNetComponent(int size)
	{
		super(size + 2, new ModulesComponent(size));
	}

}
