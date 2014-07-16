package org.xtream.core.utilities.monitors;

import java.util.List;
import java.util.Map;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;

public class CMDMonitor<T extends Component> implements Monitor<T>
{

	@Override
	public void start()
	{
		
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{	
		System.out.println("Timepoint " + timepoint + " : " + statistics.generatedStates + " / " + statistics.validStates + " / " + statistics.preferredStates + " / " + equivalenceClasses.size() + " / " + Memory.usedMemory() + " MB");
	}

	@Override
	public void stop()
	{
		
	}

}
