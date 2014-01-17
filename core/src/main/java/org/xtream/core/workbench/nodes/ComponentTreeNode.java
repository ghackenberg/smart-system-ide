package org.xtream.core.workbench.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Component;

public class ComponentTreeNode implements TreeNode
{
	
	public ComponentTreeNode parent;
	public Component component;
	
	public ComponentTreeNode(ComponentTreeNode parent, Component component)
	{
		this.parent = parent;
		this.component = component;
	}

	@Override
	public Enumeration<? extends TreeNode> children()
	{
		Vector<TreeNode> children = new Vector<>();
		
		for (Component child : component.components)
		{
			children.addElement(new ComponentTreeNode(this, child));
		}
		
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
		return new ComponentTreeNode(this, component.components.get(childIndex));
	}

	@Override
	public int getChildCount()
	{
		return component.components.size();
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof ComponentTreeNode)
		{
			ComponentTreeNode childComponent = (ComponentTreeNode) node;
			
			for (int i = 0; i < component.components.size(); i++)
			{
				if (childComponent.component == component.components.get(i))
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
		return component.components.size() == 0;
	}
	
	@Override
	public String toString()
	{
		return component.name + " : " + component.getClass().getSimpleName();
	}

}
