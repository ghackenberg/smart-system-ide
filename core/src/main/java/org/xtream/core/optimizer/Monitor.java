package org.xtream.core.optimizer;

import java.util.List;
import java.util.Map;

public interface Monitor
{
	
	public void start();
	
	public void handle(int timepoint, int generatedStates, int validStates, int dominantStates, double minObjective, double avgObjective, double maxObjective, long branch, long norm, long cluster, long sort, long stats, Map<Key, List<State>> equivalenceClasses);
	
	public void stop();

}
