package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public abstract class AbstractPhysicsComponent extends Component
{
	
	public AbstractPhysicsComponent()
	{
		super(AbstractPhysicsComponent.class.getClassLoader().getResource("physics.png"));
	}

}
