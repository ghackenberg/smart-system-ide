package org.xtream.core.workbench.events;

import org.xtream.core.model.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;

public class JumpEvent<T extends Component> extends Event<T>
{
	
	public int timepoint;

	public JumpEvent(Part<T> source, int timepoint)
	{
		super(source);
		
		this.timepoint = timepoint;
	}

}
