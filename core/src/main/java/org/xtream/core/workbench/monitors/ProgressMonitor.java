package org.xtream.core.workbench.monitors;

import java.util.List;
import java.util.Map;

import javax.swing.JProgressBar;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class ProgressMonitor extends Monitor
{
	
	private JProgressBar timeBar;
	private JProgressBar memoryBar;
	private int duration;
	
	public ProgressMonitor(JProgressBar bar, JProgressBar memoryBar, int duration)
	{
		this.timeBar = bar;
		this.memoryBar = memoryBar;
		this.duration = duration;
	}

	@Override
	public void start()
	{
		timeBar.setValue(0);
		memoryBar.setValue(getMemoryProgress());
	}

	@Override
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses)
	{
		double progress = (timepoint + 1.) / duration * 100;
		
		timeBar.setValue((int) progress);
		memoryBar.setValue(getMemoryProgress());
	}

	@Override
	public void stop()
	{
		timeBar.setValue(100);
		memoryBar.setValue(getMemoryProgress());
	}
	
	private int getMemoryProgress()
	{
		long max = maxMemory();
		long used = totalMemory() - freeMemory();
		
		double progress = (double) used / max * 100;
		
		return Math.max((int) progress, memoryBar.getValue());
	}

}
