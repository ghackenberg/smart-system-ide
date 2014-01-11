package org.xtream.core.optimizer.monitors;

import java.util.List;
import java.util.Map;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class CMDMonitor extends Monitor
{

	@Override
	public void start()
	{
		
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses)
	{	
		System.out.println("Timepoint " + timepoint + " : " + generatedStates + " / " + validStates + " / " + dominantStates + " / " + equivalenceClasses.size() + " / " + usedMemory() + " MB");
	}

	@Override
	public void stop()
	{
		
	}

}
