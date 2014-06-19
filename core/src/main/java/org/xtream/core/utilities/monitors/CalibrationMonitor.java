package org.xtream.core.utilities.monitors;

import java.util.List;
import java.util.Map;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Memory;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;

public class CalibrationMonitor<T extends Component> implements Monitor<T>
{
	
	public int timepoint;
	public int generated_states;
	public int valid_states;
	public int dominant_states;
	public int clusters;
	public double min_objective;
	public double avg_objective;
	public double max_objective;
	
	public long used_time;
	public long used_memory;

	@Override
	public void start()
	{
		used_time = System.currentTimeMillis();
	}

	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> equivalenceClasses, State best)
	{
		this.timepoint = timepoint;
		this.generated_states += statistics.generatedStates;
		this.valid_states += statistics.validStates;
		this.dominant_states += statistics.preferredStates;
		this.clusters += equivalenceClasses.size();
		this.min_objective = statistics.minObjective;
		this.avg_objective = statistics.avgObjective;
		this.max_objective = statistics.maxObjective;
		
		used_memory = Math.max(used_memory, Memory.usedMemory());
	}

	@Override
	public void stop()
	{
		used_time = System.currentTimeMillis() - used_time;
	}

}
