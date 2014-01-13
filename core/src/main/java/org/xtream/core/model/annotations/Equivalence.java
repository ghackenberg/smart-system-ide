package org.xtream.core.model.annotations;

import org.xtream.core.model.Annotation;
import org.xtream.core.model.Port;

public class Equivalence extends Annotation<Double>
{

	public Equivalence(Port<Double> port)
	{
		super(port);
	}

}
