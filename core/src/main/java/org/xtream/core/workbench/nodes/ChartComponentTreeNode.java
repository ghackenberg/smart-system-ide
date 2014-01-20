package org.xtream.core.workbench.nodes;

import org.xtream.core.model.Component;

public class ChartComponentTreeNode extends AbstractComponentTreeNode
{
	
	public ChartComponentTreeNode(ChartComponentTreeNode parent, Component component)
	{
		super(parent, component);
		
		for (Component child : component.components)
		{
			if (child.chartsRecursive.size() > 0)
			{
				children.addElement(new ChartComponentTreeNode(this, child));
			}
		}
	}
	
}
