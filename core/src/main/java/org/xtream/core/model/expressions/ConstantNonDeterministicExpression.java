package org.xtream.core.model.expressions;

import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.builders.SetBuilder;

public class ConstantNonDeterministicExpression<T> extends NonDeterministicExpression<T>
{
	
	@Constant
	public Set<T> set;
	
	public ConstantNonDeterministicExpression(Port<T> port, SetBuilder<T> builder)
	{
		super(port);
		
		this.set = builder.set;
	}

	@Override
	protected Set<T> evaluateSet(int timepoint)
	{
		return set;
	}

}
