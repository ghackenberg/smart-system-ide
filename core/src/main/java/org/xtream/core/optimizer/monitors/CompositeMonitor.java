package org.xtream.core.optimizer.monitors;

import java.util.ArrayList;
import java.util.List;

import org.xtream.core.optimizer.Monitor;

public class CompositeMonitor extends Monitor
{
	
	private List<Monitor> monitors = new ArrayList<>();
	
	public CompositeMonitor(Monitor... monitors)
	{
		for (Monitor monitor : monitors)
		{
			this.monitors.add(monitor);
		}
	}

	@Override
	public void start()
	{
		for (Monitor monitor : monitors)
		{
			monitor.start();
		}
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, int equivalenceClasses)
	{
		for (Monitor monitor : monitors)
		{
			monitor.handle(timepoint, generatedStates, validStates, dominantStates, equivalenceClasses);
		}
	}

	@Override
	public void stop()
	{
		for (Monitor monitor : monitors)
		{
			monitor.stop();
		}
	}

}
