package org.xtream.core.optimizer;

import java.util.List;
import java.util.Map;

public abstract class Monitor
{
	
	protected long totalMemory()
	{
		return Runtime.getRuntime().totalMemory() / 1024 / 1024;
	}
	
	protected long freeMemory()
	{
		return Runtime.getRuntime().freeMemory() / 1024 / 1024;
	}
	
	protected long maxMemory()
	{
		return Runtime.getRuntime().maxMemory() / 1024 / 1024;
	}
	
	protected long usedMemory()
	{
		return totalMemory() - freeMemory();
	}
	
	public abstract void start();
	
	public abstract void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses);
	
	public abstract void stop();

}
