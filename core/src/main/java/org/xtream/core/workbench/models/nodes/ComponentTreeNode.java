package org.xtream.core.workbench.models.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Component;

public class ComponentTreeNode implements TreeNode
{
	
	public ComponentTreeNode parent;
	public Component component;
	public Vector<ComponentTreeNode> children = new Vector<>();
	
	public ComponentTreeNode(ComponentTreeNode parent, Component component)
	{
		this.parent = parent;
		this.component = component;
		
		for (Component child : component.getChildrenByClass(Component.class))
		{
			children.addElement(new ComponentTreeNode(this, child));
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
		if (node instanceof ComponentTreeNode)
		{
			ComponentTreeNode childComponent = (ComponentTreeNode) node;
			
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

	
	@Override
	public TreeNode getParent()
	{
		return parent;
	}
	
	@Override
	public String toString()
	{
		return component.getName();
	}

}
