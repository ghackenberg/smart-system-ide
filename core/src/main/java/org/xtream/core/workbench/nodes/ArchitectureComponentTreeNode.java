package org.xtream.core.workbench.nodes;

import org.xtream.core.model.Component;

public class ArchitectureComponentTreeNode extends AbstractComponentTreeNode
{
	
	public ArchitectureComponentTreeNode(ArchitectureComponentTreeNode parent, Component component)
	{
		super(parent, component);
		
		for (Component child : component.components)
		{
			if (child.components.size() > 0)
			{
				children.addElement(new ArchitectureComponentTreeNode(this, child));
			}
		}
	}

	
}
