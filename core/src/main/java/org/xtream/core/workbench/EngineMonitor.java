package org.xtream.core.workbench;

import java.util.List;
import java.util.Map;

import javax.swing.JProgressBar;
import javax.swing.JSlider;

import org.xtream.core.model.Component;
import org.xtream.core.model.State;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.Statistics;

public class EngineMonitor<T extends Component> implements Monitor<T>
{
	
	private JProgressBar timeBar;
	private JProgressBar memoryBar;
	private JSlider slider;
	private int duration;
	
	public EngineMonitor(JProgressBar bar, JProgressBar memoryBar, JSlider slider, int duration)
	{
		this.timeBar = bar;
		this.memoryBar = memoryBar;
		this.slider = slider;
		this.duration = duration;
	}

	@Override
	public void start()
	{
		timeBar.setValue(0);
		memoryBar.setValue(getMemoryProgress());
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		double progress = (timepoint + 1.) / duration * 100;
		
		timeBar.setValue((int) progress);
		memoryBar.setValue(getMemoryProgress());
		slider.setMaximum(timepoint);
		slider.setValue(timepoint);
	}

	@Override
	public void stop()
	{
		timeBar.setValue(100);
		memoryBar.setValue(getMemoryProgress());
	}
	
	private int getMemoryProgress()
	{
		long max = Memory.maxMemory();
		long used = Memory.usedMemory();
		
		double progress = (double) used / max * 100;
		
		return Math.max((int) progress, memoryBar.getValue());
	}

}
