package org.xtream.core.optimizer;

import org.xtream.core.model.State;

public class Comparator implements java.util.Comparator<State>
{

	@Override public int compare(State o1, State o2)
	{
		return o1.compareObjectiveTo(o2);
	}

}
