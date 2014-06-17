package org.xtream.core.model.expressions;

import java.util.HashSet;
import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class ConstantNonDeterministicExpression<T> extends NonDeterministicExpression<T>
{
	
	private Set<T> set;
	
	public ConstantNonDeterministicExpression(Port<T> port, Set<T> set)
	{
		super(port);
		
		this.set = set;
	}
	
	@SafeVarargs
	public ConstantNonDeterministicExpression(Port<T> port, T... values)
	{
		super(port);
		
		set = new HashSet<>();
		
		for (T value : values)
		{
			set.add(value);
		}
	}

	@Override
	protected Set<T> evaluateSet(State state, int timepoint)
	{
		return set;
	}

}
