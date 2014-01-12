package org.xtream.core.model.expressions;

import java.util.Map;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.builders.MapBuilder;

public class ConstantProbabilisticExpression<T> extends ProbabilisticExpression<T>
{
	
	@Constant
	public Map<T, Double> map;
	
	public ConstantProbabilisticExpression(Port<T> port, MapBuilder<T> builder)
	{
		super(port);
		
		this.map = builder.map;
	}

	@Override
	protected Map<T, Double> evaluateDistribution(int timepoint)
	{
		return map;
	}
	
}
