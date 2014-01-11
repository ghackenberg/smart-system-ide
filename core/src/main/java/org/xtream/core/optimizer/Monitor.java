package org.xtream.core.optimizer;

public abstract class Monitor
{
	
	public abstract void start();
	
	public abstract void handle(int timepoint, int generatedStates, int validStates, int dominantStates, int equivalenceClasses);
	
	public abstract void stop();

}
