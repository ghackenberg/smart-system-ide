package org.xtream.core.workbench.monitors;

import java.util.List;
import java.util.Map;

import javax.swing.JProgressBar;
import javax.swing.JSlider;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;

public class ProgressMonitor<T extends Component> implements Monitor<T>
{
	
	private T root;
	private JProgressBar timeBar;
	private JProgressBar memoryBar;
	private JSlider slider;
	private int duration;
	
	public ProgressMonitor(T root, JProgressBar bar, JProgressBar memoryBar, JSlider slider, int duration)
	{
		this.root = root;
		this.timeBar = bar;
		this.memoryBar = memoryBar;
		this.slider = slider;
		this.duration = duration;
	}

	@Override
	public void start(T root)
	{
		timeBar.setValue(0);
		memoryBar.setValue(getMemoryProgress());
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		double progress = (timepoint + 1.) / duration * 100;
		
		best.restore(root);
		timeBar.setValue((int) progress);
		memoryBar.setValue(getMemoryProgress());
		slider.setMaximum(timepoint);
		slider.setValue(timepoint);
	}

	@Override
	public void stop(T root, int timepoint)
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
