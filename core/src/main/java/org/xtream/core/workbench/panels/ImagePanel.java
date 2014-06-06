package org.xtream.core.workbench.panels;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	
	private static final long serialVersionUID = -556945236338956136L;
	
	private Image image;
	
	public void setImage(Image image)
	{
		this.image = image;
		
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if (image != null)
		{
			float imageRatio = (float) image.getWidth(null) / image.getHeight(null);
			float panelRatio = (float) g.getClipBounds().width / g.getClipBounds().height;
			
			int x = 0;
			int y = 0;
			int width = g.getClipBounds().width;
			int height = g.getClipBounds().height;
			
			if (imageRatio > panelRatio)
			{
				height /= imageRatio;
				y = (g.getClipBounds().height - height) / 2;
			}
			else
			{
				width *= imageRatio;
				x = (g.getClipBounds().width - width) / 2;
			}
			
			g.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), x, y, width, height, null);
		}
	}

}
