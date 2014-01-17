package org.xtream.core.workbench.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Component;

public class CompositeComponentTreeNode implements TreeNode
{
	
	public CompositeComponentTreeNode parent;
	public Component component;
	public Vector<CompositeComponentTreeNode> children;
	
	public CompositeComponentTreeNode(CompositeComponentTreeNode parent, Component component)
	{
		this.parent = parent;
		this.component = component;
		
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
	public TreeNode getParent()
	{
		return parent;
	}

	@Override
	public boolean isLeaf()
	{
		return children.size() == 0;
	}
	
	@Override
	public String toString()
	{
		return component.name;
	}

}
