package org.xtream.core.model.annotations;

import org.xtream.core.model.Annotation;
import org.xtream.core.model.Port;

public class Constraint extends Annotation<Boolean>
{

	public Constraint(Port<Boolean> port)
	{
		super(port);
	}

}
