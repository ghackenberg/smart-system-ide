package org.xtream.core.workbench;

import javax.swing.JTabbedPane;

import org.xtream.core.model.Component;

public abstract class Viewer<T extends Component> extends org.xtream.core.optimizer.Viewer<T>
{
	
	protected JTabbedPane tabs;
	
	public Viewer(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

}
