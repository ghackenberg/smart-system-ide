package org.xtream.core.workbench.models.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;
import org.xtream.core.model.Container;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class ContainerTreeTableNode implements TreeTableNode
{

	public ContainerTreeTableNode parent;
	public Container container;
	public State state;
	public int timepoint;
	
	public ContainerTreeTableNode(ContainerTreeTableNode parent, Container container, State state, int timepoint)
	{
		this.parent = parent;
		this.container = container;
		this.state = state;
		this.timepoint = timepoint;
	}

	@Override
	public boolean getAllowsChildren()
	{
		return true;
	}

	@Override
	public int getChildCount()
	{
		return container.getChildrenByClass(Port.class).size() + container.getChildrenByClass(Container.class).size();
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof ContainerTreeTableNode)
		{
			ContainerTreeTableNode containerNode = (ContainerTreeTableNode) node;
			
			Container argumentContainer = containerNode.container;
			
			for (int index = 0; index < argumentContainer.getChildrenByClass(Container.class).size(); index++)
			{
				if (container == argumentContainer.getChildrenByClass(Container.class).get(index))
				{
					return argumentContainer.getChildrenByClass(Port.class).size() + index;
				}
			}
		}
		return -1;
	}

	@Override
	public boolean isLeaf()
	{
		return container.getChildrenByClass(Port.class).size() == 0 && container.getChildrenByClass(Container.class).size() == 0;
	}

	@Override
	public Enumeration<? extends TreeTableNode> children()
	{
		Vector<TreeTableNode> children = new Vector<>();
		
		for (Port<?> port : container.getChildrenByClass(Port.class))
		{
			children.add(new PortTreeTableNode(this, port, state, timepoint));
		}
		for (Container childContainer : container.getChildrenByClass(Container.class))
		{
			children.add(new ContainerTreeTableNode(this, childContainer, state, timepoint));
		}
		
		return children.elements();
	}

	@Override
	public TreeTableNode getChildAt(int index)
	{
		if (index < container.getChildrenByClass(Port.class).size())
		{
			return new PortTreeTableNode(this, container.getChildrenByClass(Port.class).get(index), state, timepoint);
		}
		else
		{
			return new ContainerTreeTableNode(this, container.getChildrenByClass(Container.class).get(index - container.getChildrenByClass(Port.class).size()), state, timepoint);
		}
	}

	@Override
	public int getColumnCount()
	{
		return timepoint + 1;
	}

	@Override
	public TreeTableNode getParent()
	{
		return parent;
	}

	@Override
	public Object getUserObject()
	{
		return container;
	}

	@Override
	public Object getValueAt(int column)
	{
		if (column == 0)
		{
			return container.getName() + " : " + container.getClass().getSimpleName();
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean isEditable(int column)
	{
		return false;
	}

	@Override
	public void setUserObject(Object object)
	{
		throw new IllegalStateException();
	}

	@Override
	public void setValueAt(Object value, int column)
	{
		throw new IllegalStateException();
	}
	
}
