package org.xtream.core.optimizer.monitors;

import java.util.List;
import java.util.Map;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;

public class CMDMonitor<T extends Component> implements Monitor<T>
{

	@Override
	public void start(T root)
	{
		
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{	
		System.out.println("Timepoint " + timepoint + " : " + statistics.generatedStates + " / " + statistics.validStates + " / " + statistics.dominantStates + " / " + equivalenceClasses.size() + " / " + Memory.usedMemory() + " MB");
	}

	@Override
	public void stop(T root, int timepoint)
	{
		
	}

}
