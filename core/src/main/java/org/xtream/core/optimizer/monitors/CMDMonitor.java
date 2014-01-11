package org.xtream.core.optimizer.monitors;

import org.xtream.core.optimizer.Monitor;

public class CMDMonitor extends Monitor
{

	@Override
	public void start()
	{
		
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, int equivalenceClasses)
	{	
		System.out.println("Timepoint " + timepoint + " : " + generatedStates + " / " + validStates + " / " + dominantStates + " / " + equivalenceClasses + " / " + usedMemory() + " MB");
	}

	@Override
	public void stop()
	{
		
	}

}
