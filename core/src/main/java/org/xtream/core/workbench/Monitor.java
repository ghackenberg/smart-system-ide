package org.xtream.core.workbench;

import javax.swing.JTabbedPane;

public abstract class Monitor extends org.xtream.core.optimizer.Monitor
{
	
	protected JTabbedPane tabs;
	
	public Monitor(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

}
