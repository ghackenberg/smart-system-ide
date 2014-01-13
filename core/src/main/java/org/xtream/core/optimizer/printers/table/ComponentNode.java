package org.xtream.core.optimizer.printers.table;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class ComponentNode implements TreeTableNode
{

	public ComponentNode parent;
	public Component component;
	public int timepoint;
	
	public ComponentNode(ComponentNode parent, Component component, int timepoint)
	{
		this.parent = parent;
		this.component = component;
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
		return component.ports.size() + component.components.size();
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof ComponentNode)
		{
			ComponentNode componentNode = (ComponentNode) node;
			
			Component argumentComponent = componentNode.component;
			
			for (int index = 0; index < argumentComponent.components.size(); index++)
			{
				if (component == argumentComponent.components.get(index))
				{
					return argumentComponent.ports.size() + index;
				}
			}
		}
		return -1;
	}

	@Override
	public boolean isLeaf()
	{
		return component.ports.size() == 0 && component.components.size() == 0;
	}

	@Override
	public Enumeration<? extends TreeTableNode> children()
	{
		Vector<TreeTableNode> children = new Vector<>();
		
		for (Port<?> port : component.ports)
		{
			children.add(new PortNode(this, port));
		}
		for (Component childComponent : component.components)
		{
			children.add(new ComponentNode(this, childComponent, timepoint));
		}
		
		return children.elements();
	}

	@Override
	public TreeTableNode getChildAt(int index)
	{
		if (index < component.ports.size())
		{
			return new PortNode(this, component.ports.get(index));
		}
		else
		{
			return new ComponentNode(this, component.components.get(index - component.ports.size()), timepoint);
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
		return component;
	}

	@Override
	public Object getValueAt(int column)
	{
		if (column == 0)
		{
			return component.name;
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
