package org.xtream.core.optimizer.monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class CompositeMonitor implements Monitor
{
	
	private List<Monitor> monitors = new ArrayList<>();
	
	public CompositeMonitor(Monitor... monitors)
	{
		for (Monitor monitor : monitors)
		{
			this.monitors.add(monitor);
		}
	}
	
	public void add(Monitor monitor)
	{
		monitors.add(monitor);
	}
	
	public void remove(Monitor monitor)
	{
		monitors.remove(monitor);
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
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, long branch, long norm, long cluster, long sort, long stats, Map<Key, List<State>> equivalenceClasses)
	{
		for (Monitor monitor : monitors)
		{
			monitor.handle(timepoint, generatedStates, validStates, dominantStates, minObjective, avgObjective, maxObjective, branch, norm, cluster, sort, stats, equivalenceClasses);
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
