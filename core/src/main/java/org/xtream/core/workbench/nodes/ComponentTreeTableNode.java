package org.xtream.core.workbench.nodes;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class ComponentTreeTableNode implements TreeTableNode
{

	public ComponentTreeTableNode parent;
	public Component component;
	public State state;
	public int timepoint;
	
	public ComponentTreeTableNode(ComponentTreeTableNode parent, Component component, State state, int timepoint)
	{
		this.parent = parent;
		this.component = component;
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
		return component.getChildrenByClass(Port.class).size() + component.getChildrenByClass(Component.class).size();
	}

	@Override
	public int getIndex(TreeNode node)
	{
		if (node instanceof ComponentTreeTableNode)
		{
			ComponentTreeTableNode componentNode = (ComponentTreeTableNode) node;
			
			Component argumentComponent = componentNode.component;
			
			for (int index = 0; index < argumentComponent.getChildrenByClass(Component.class).size(); index++)
			{
				if (component == argumentComponent.getChildrenByClass(Component.class).get(index))
				{
					return argumentComponent.getChildrenByClass(Port.class).size() + index;
				}
			}
		}
		return -1;
	}

	@Override
	public boolean isLeaf()
	{
		return component.getChildrenByClass(Port.class).size() == 0 && component.getChildrenByClass(Component.class).size() == 0;
	}

	@Override
	public Enumeration<? extends TreeTableNode> children()
	{
		Vector<TreeTableNode> children = new Vector<>();
		
		for (Port<?> port : component.getChildrenByClass(Port.class))
		{
			children.add(new PortTreeTableNode(this, port, state, timepoint));
		}
		for (Component childComponent : component.getChildrenByClass(Component.class))
		{
			children.add(new ComponentTreeTableNode(this, childComponent, state, timepoint));
		}
		
		return children.elements();
	}

	@Override
	public TreeTableNode getChildAt(int index)
	{
		if (index < component.getChildrenByClass(Port.class).size())
		{
			return new PortTreeTableNode(this, component.getChildrenByClass(Port.class).get(index), state, timepoint);
		}
		else
		{
			return new ComponentTreeTableNode(this, component.getChildrenByClass(Component.class).get(index - component.getChildrenByClass(Port.class).size()), state, timepoint);
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
			return component.getName() + " : " + component.getClass().getSimpleName();
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
