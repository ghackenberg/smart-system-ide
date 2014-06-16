package org.xtream.core.model.expressions;

import java.util.HashMap;
import java.util.Map;

import org.xtream.core.model.Pair;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class ConstantProbabilisticExpression<T> extends ProbabilisticExpression<T>
{
	
	@Constant
	public Map<T, Double> map;
	
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
