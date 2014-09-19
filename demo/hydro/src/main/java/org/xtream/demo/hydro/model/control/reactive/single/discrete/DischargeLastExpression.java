package org.xtream.demo.hydro.model.control.reactive.single.discrete;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.NonDeterministicExpression;
import org.xtream.demo.hydro.model.Constants;

public class DischargeLastExpression extends NonDeterministicExpression<Double>
{
	
	public DischargeLastExpression(Port<Double> port)
	{
		super(port);
	}
	
	@Override protected Set<Double> evaluateSet(State state, int timepoint)
	{
		double previous;
		
		if (timepoint == 0)
		{
			previous = Constants.DATASET.getOutflowTotal(5, Constants.START + timepoint - 1);
		}
		else
		{
			previous = getPort().get(state, timepoint - 1);	
		}
		
		double min = Math.max(previous * 0.95, 1);
		double max = Math.min(previous * 1.125, Constants.WEHR4_DISCHARGE_MAX);
		
		Set<Double> result = new HashSet<Double>();
		
		for (int i = 0; i <= Constants.WEHR4_STEPS; i++)
		{
			result.add(min + (max - min) * i / Constants.WEHR4_STEPS);
		}
		
		return result;
	}

}
