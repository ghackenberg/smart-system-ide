package org.xtream.core.optimizer.beam;

import java.util.List;
import java.util.SortedMap;

import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;

public interface Strategy
{
	
	public SortedMap<Key, List<State>> execute(List<State> currentStates, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint, Component root);

}
