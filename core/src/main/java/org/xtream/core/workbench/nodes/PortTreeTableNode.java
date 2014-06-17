package org.xtream.core.workbench.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class PortTreeTableNode implements TreeTableNode
{
	
	public ComponentTreeTableNode parent;
	public Port<?> port;
	public State state;
	public int timepoint;
	
	public PortTreeTableNode(ComponentTreeTableNode parent, Port<?> port, State state, int timepoint)
	{
		this.parent = parent;
		this.port = port;
		this.state = state;
		this.timepoint = timepoint;
	}

	@Override
	public boolean getAllowsChildren()
	{
		return false;
	}

	@Override
	public int getChildCount()
	{
		return 0;
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof ComponentTreeTableNode)
		{
			ComponentTreeTableNode componentNode = (ComponentTreeTableNode) node;
			
			Component component = componentNode.component;
			
			for (int index = 0; index < component.getChildrenByClass(Port.class).size(); index++)
			{
				if (port == component.getChildrenByClass(Port.class).get(index))
				{
					return index;
				}
			}
		}
		
		return -1;
	}

	@Override
	public boolean isLeaf()
	{
		return true;
	}

	@Override
	public Enumeration<? extends TreeTableNode> children()
	{
		return null;
	}

	@Override
	public TreeTableNode getChildAt(int index)
	{
		return null;
	}

	@Override
	public int getColumnCount()
	{
		return timepoint + 2;
	}

	@Override
	public TreeTableNode getParent()
	{
		return parent;
	}

	@Override
	public Object getUserObject()
	{
		return port;
	}

	@Override
	public Object getValueAt(int column)
	{
		if (column == 0)
		{
			return port.getName();
		}
		else
		{
			return port.get(state, column - 1);
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
	public void setValueAt(Object object, int column)
	{
		throw new IllegalStateException();
	}

}
