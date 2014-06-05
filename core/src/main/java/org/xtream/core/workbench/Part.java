package org.xtream.core.workbench;

import javax.swing.JTabbedPane;

public abstract class Part
{
	
	protected JTabbedPane tabs;
	
	public void initialize(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

}
