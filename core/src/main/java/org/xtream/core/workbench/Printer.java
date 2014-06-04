package org.xtream.core.workbench;

import javax.swing.JTabbedPane;

import org.xtream.core.model.Component;

public abstract class Printer<T extends Component> extends org.xtream.core.optimizer.Printer<T>
{
	
	protected JTabbedPane tabs;
	
	public Printer(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

}
