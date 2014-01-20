package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public abstract class AbstractLogicsComponent extends Component
{
	
	public AbstractLogicsComponent()
	{
		super(AbstractLogicsComponent.class.getClassLoader().getResource("logics.png"));
	}

}
