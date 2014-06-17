package org.xtream.core.utilities.visitors;

import org.xtream.core.model.Component;
import org.xtream.core.utilities.Visitor;

public class SampleVisitor extends Visitor
{
	
	public void handle(Component component)
	{
		System.out.println(component.getQualifiedName());
	}

}
