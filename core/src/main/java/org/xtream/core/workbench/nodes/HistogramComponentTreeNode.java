package org.xtream.core.workbench.nodes;

import org.xtream.core.model.Component;

public class HistogramComponentTreeNode extends AbstractComponentTreeNode
{
	
	public HistogramComponentTreeNode(HistogramComponentTreeNode parent, Component component)
	{
		super(parent, component);
		
		for (Component child : component.components)
		{
			if (child.histogramsRecursive.size() > 0)
			{
				children.addElement(new HistogramComponentTreeNode(this, child));
			}
		}
	}
	
}
