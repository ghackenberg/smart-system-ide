package org.xtream.core.optimizer.printers.nodes;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class PortNode implements TreeTableNode
{
	
	public ComponentNode parent;
	public Port<?> port;
	
	public PortNode(ComponentNode parent, Port<?> port)
	{
		this.parent = parent;
		this.port = port;
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
		if (node instanceof ComponentNode)
		{
			ComponentNode componentNode = (ComponentNode) node;
			
			Component component = componentNode.component;
			
			for (int index = 0; index < component.ports.size(); index++)
			{
				if (port == component.ports.get(index))
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
		return port.state.timepoint + 2;
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
			return port.name;
		}
		else
		{
			return port.get(column - 1);
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
