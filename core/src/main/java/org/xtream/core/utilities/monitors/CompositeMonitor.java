package org.xtream.core.utilities.monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;

public class CompositeMonitor<T extends Component> implements Monitor<T>
{
	
	private List<Monitor<T>> monitors = new ArrayList<>();
	
	@SafeVarargs
	public CompositeMonitor(Monitor<T>... monitors)
	{
		for (Monitor<T> monitor : monitors)
		{
			this.monitors.add(monitor);
		}
	}
	
	public void add(Monitor<T> monitor)
	{
		monitors.add(monitor);
	}
	
	public void remove(Monitor<T> monitor)
	{
		monitors.remove(monitor);
	}

	@Override
	public void start()
	{
		for (Monitor<T> monitor : monitors)
		{
			monitor.start();
		}
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		for (Monitor<T> monitor : monitors)
		{
			monitor.handle(timepoint, statistics, equivalenceClasses, best);
		}
	}

	@Override
	public void stop()
	{
		for (Monitor<T> monitor : monitors)
		{
			monitor.stop();
		}
	}

}
