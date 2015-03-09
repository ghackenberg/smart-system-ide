package org.xtream.core.model.expressions;

import java.util.HashMap;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;
import org.xtream.core.model.State;

public class DynamicChannelExpression<S, T> extends Expression<T>
{
	@Reference
	private Port<T> target;
	private Port<S> condition;
	private HashMap<Port<S>,Port<T>> sourceRegister; // key = condition, value = source 

	public DynamicChannelExpression(Port<T> target, Port<S> condition, HashMap<Port<S>, Port<T>> sourceRegister)
	{
		super(target);
		this.target = target;
		this.condition = condition;
		this.sourceRegister = sourceRegister;
	}

	@Override
	protected T evaluate(State state, int timepoint)
	{
		for (Port<S> key : sourceRegister.keySet())
		{
			if (key.get(state, timepoint).equals(condition.get(state, timepoint)))
			{
				return sourceRegister.get(key).get(state, timepoint);
			}
		}
		
		return null;
	}
		
}
