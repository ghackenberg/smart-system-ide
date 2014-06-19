package org.xtream.core.optimizer;

public class Comparator implements java.util.Comparator<State>
{

	@Override public int compare(State o1, State o2)
	{
		return o1.compareObjectiveTo(o2);
	}

}
