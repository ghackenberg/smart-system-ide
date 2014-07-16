package org.xtream.core.workbench.events;

import org.xtream.core.model.containers.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;

public class JumpEvent<T extends Component> extends Event<T>
{
	
	private int timepoint;

	public JumpEvent(Part<T> source, int timepoint)
	{
		super(source);
		
		this.timepoint = timepoint;
	}
	
	public int getTimepoint()
	{
		return timepoint;
	}

}
