package org.xtream.core.model.expressions;

import java.util.HashMap;
import java.util.Map;

import org.xtream.core.data.Pair;
import org.xtream.core.model.Port;

public class ConstantProbabilisticExpression<T> extends ProbabilisticExpression<T>
{
	
	private Map<T, Double> map;
	
	public ConstantProbabilisticExpression(Port<T> port, Map<T, Double> map)
	{
		super(port);
		
		this.map = map;
	}
	
	@SafeVarargs
	public ConstantProbabilisticExpression(Port<T> port, Pair<T>... values)
	{
		super(port);
		
		map = new HashMap<>();
		
		for (Pair<T> pair : values)
		{
			map.put(pair.key, pair.value);
		}
	}

	@Override
	protected Map<T, Double> evaluateDistribution(int timepoint)
	{
		return map;
	}
	
}
