package org.xtream.core.optimizer;

import java.util.Map;

import org.xtream.core.model.markers.Constraint;

public class Statistics
{
	
	// TODO make fields private and provide getters/setters
	
	public int generatedStates;
	public int validStates;
	public int preferredStates;
	public double minObjective;
	public double avgObjective;
	public double maxObjective;
	public long branch;
	public long norm;
	public long cluster;
	public long sort;
	public long stats;
	public Map<Constraint, Integer> violations;
	public int zeroOptionCount;

}
