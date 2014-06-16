package org.xtream.core.workbench;

import java.awt.GridLayout;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.xtream.core.model.Component;


public abstract class Part<T extends Component>
{
	private String title;
	private Icon icon;
	private JPanel panel;
	private int x;
	private int y;
	private int width;
	private int height;
	private Bus<T> bus;
	private T root;
	
	public Part(String title, int x, int y, int width, int height)
	{
		this(title, Part.class.getClassLoader().getResource("part.png"), x, y, width, height);
	}
	public Part(String title, URL icon, int x, int y, int width, int height)
	{
		this.title = title;
		this.icon = new ImageIcon(icon);
		this.panel = new JPanel(new GridLayout(1, 1));
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public void setRoot(T root)
	{
		this.root = root;
	}
	public T getRoot()
	{
		return root;
	}
	
	public void setBus(Bus<T> bus)
	{
		this.bus = bus;
		
		bus.components.add(this);
	}
	public Bus<T> getBus()
	{
		return bus;
	}
	
	public void handle(Event<T> event)
	{
		
	}
	public void trigger(Event<T> event)
	{
		for (Part<T> component : bus.components)
		{
			if (component != this)
			{
				component.handle(event);
			}
		}
	}
	
}
