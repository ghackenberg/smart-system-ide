package org.xtream.core.model.annotations;

import org.xtream.core.model.Annotation;
import org.xtream.core.model.Port;

public class Equivalence<T> extends Annotation<T>
{

	public Equivalence(Port<T> port)
	{
		super(port);
	}

}
