package org.xtream.core.optimizer;

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
	
	public abstract void handle(int timepoint, int generatedStates, int validStates, int dominantStates, int equivalenceClasses);
	
	public abstract void stop();

}
