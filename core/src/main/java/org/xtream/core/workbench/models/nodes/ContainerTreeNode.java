package org.xtream.core.workbench.models.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.xtream.core.model.Container;

public class ContainerTreeNode implements TreeNode
{
	
	public ContainerTreeNode parent;
	public Container container;
	public Vector<ContainerTreeNode> children = new Vector<>();
	
	public ContainerTreeNode(ContainerTreeNode parent, Container container)
	{
		this.parent = parent;
		this.container = container;
		
		for (Container child : container.getChildrenByClass(Container.class))
		{
			children.addElement(new ContainerTreeNode(this, child));
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
		if (node instanceof ContainerTreeNode)
		{
			ContainerTreeNode childComponent = (ContainerTreeNode) node;
			
			for (int i = 0; i < children.size(); i++)
			{
				if (childComponent.container == children.get(i).container)
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
		return container.getName();
	}

}
