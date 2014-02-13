package org.xtream.core.workbench.renderers;

import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;

public class ComponentTreeCellRenderer extends DefaultTreeCellRenderer
{

	private static final long serialVersionUID = -5343638159221108825L;
	
	private Map<URL, ImageIcon> icons = new HashMap<>();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,hasFocus);
		
		AbstractComponentTreeNode node = (AbstractComponentTreeNode) value;
		
		if (node.component.icon != null)
		{	
			ImageIcon icon = icons.get(node.component.icon);
			
			if (icon == null)
			{
				ImageIcon image = new ImageIcon(node.component.icon);
				
				if (image.getIconWidth() == 16 && image.getIconHeight() == 16)
				{
					icon = image;
				}
				else
				{
					icon = new ImageIcon(image.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
				}
				
				icons.put(node.component.icon, icon);
			}
			
			setIcon(icon);
		}
		
		return this;
	}

}