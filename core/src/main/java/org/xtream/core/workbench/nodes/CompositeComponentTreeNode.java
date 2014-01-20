package org.xtream.core.workbench.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Component;

public class CompositeComponentTreeNode extends AbstractComponentTreeNode
{
	
	public Vector<CompositeComponentTreeNode> children;
	
	public CompositeComponentTreeNode(CompositeComponentTreeNode parent, Component component)
	{
		super(parent, component);
		
		children = new Vector<>();
		
		for (Component child : component.components)
		{
			if (child.components.size() > 0)
			{
				children.addElement(new CompositeComponentTreeNode(this, child));
			}
		}
	}

	@Override
	public Enumeration<? extends TreeNode> children()
	{
		return children.elements();
	}

	@Override
	public boolean getAllowsChildren()
	{
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return children.get(childIndex);
	}

	@Override
	public int getChildCount()
	{
		return children.size();
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof CompositeComponentTreeNode)
		{
			CompositeComponentTreeNode childComponent = (CompositeComponentTreeNode) node;
			
			for (int i = 0; i < children.size(); i++)
			{
				if (childComponent.component == children.get(i).component)
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	
	@Override
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

}
