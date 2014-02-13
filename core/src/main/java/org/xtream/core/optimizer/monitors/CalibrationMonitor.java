package org.xtream.core.optimizer.monitors;

import java.util.List;
import java.util.Map;

import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.Monitor;
import org.xtream.core.optimizer.State;

public class CalibrationMonitor extends Monitor
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
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, Map<Key, List<State>> equivalenceClasses)
	{
		this.timepoint = timepoint;
		this.generated_states += generatedStates;
		this.valid_states += validStates;
		this.dominant_states += dominantStates;
		this.clusters += equivalenceClasses.size();
		this.min_objective = minObjective;
		this.avg_objective = avgObjective;
		this.max_objective = maxObjective;
		
		used_memory = Math.max(used_memory, usedMemory());
	}

	@Override
	public void stop()
	{
		used_time = System.currentTimeMillis() - used_time;
	}

}
