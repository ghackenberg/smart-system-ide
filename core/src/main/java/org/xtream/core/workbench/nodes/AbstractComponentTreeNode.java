package org.xtream.core.workbench.nodes;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Component;

public abstract class AbstractComponentTreeNode implements TreeNode
{
	
	public AbstractComponentTreeNode parent;
	public Component component;
	
	public AbstractComponentTreeNode(AbstractComponentTreeNode parent, Component component)
	{
		this.parent = parent;
		this.component = component;
	}
	
	@Override
	public TreeNode getParent()
	{
		return parent;
	}
	
	@Override
	public String toString()
	{
		return component.name + " : " + component.getClass().getSimpleName();
	}

}
