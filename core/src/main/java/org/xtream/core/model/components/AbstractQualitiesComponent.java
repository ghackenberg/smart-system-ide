package org.xtream.core.model.components;

import org.xtream.core.model.Component;

public abstract class AbstractQualitiesComponent extends Component
{
	
	public AbstractQualitiesComponent()
	{
		super(AbstractQualitiesComponent.class.getClassLoader().getResource("qualities.png"));
	}

}
