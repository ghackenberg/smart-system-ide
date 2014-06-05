package org.xtream.core.workbench;

import java.awt.GridLayout;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public abstract class Part
{
	private String title;
	private Icon icon;
	private JPanel panel;
	
	public Part(String title)
	{
		this(title, Part.class.getClassLoader().getResource("part.png"));
	}
	public Part(String title, URL icon)
	{
		this.title = title;
		this.icon = new ImageIcon(icon);
		this.panel = new JPanel(new GridLayout(1, 1));
	}
	
	public String getTitle()
	{
		return title;
	}
	protected void setTitle(String title)
	{
		this.title = title;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	protected void setIcon(Icon icon)
	{
		this.icon = icon;
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
}
