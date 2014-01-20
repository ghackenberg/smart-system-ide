package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public abstract class AbstractConstraintsComponent extends Component
{
	
	public AbstractConstraintsComponent()
	{
		super(AbstractConstraintsComponent.class.getClassLoader().getResource("constraints.png"));
	}

}
