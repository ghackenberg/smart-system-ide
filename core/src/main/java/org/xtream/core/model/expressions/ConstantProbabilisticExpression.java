package org.xtream.core.model.expressions;

import java.util.Map;

import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.builders.MapBuilder;
import org.xtream.core.model.ports.OutputPort;

public class ConstantProbabilisticExpression<T> extends ProbabilisticExpression<T>
{
	
	@Constant
	public Map<T, Double> map;
	
	public ConstantProbabilisticExpression(OutputPort<T> port, MapBuilder<T> builder)
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
