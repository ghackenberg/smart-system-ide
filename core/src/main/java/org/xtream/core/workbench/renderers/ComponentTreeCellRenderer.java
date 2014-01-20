package org.xtream.core.workbench.renderers;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;

public class ComponentTreeCellRenderer extends DefaultTreeCellRenderer
{

	private static final long serialVersionUID = -5343638159221108825L;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,hasFocus);
		
		AbstractComponentTreeNode node = (AbstractComponentTreeNode) value;
		
		if (node.component.icon != null)
		{	
			ImageIcon icon = new ImageIcon(node.component.icon);
			
			setIcon(new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		}
		
		return this;
	}

}
