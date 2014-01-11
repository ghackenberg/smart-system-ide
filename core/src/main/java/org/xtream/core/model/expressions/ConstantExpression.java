package org.xtream.core.model.expressions;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class ConstantExpression<T> extends Expression<T>
{

	@Constant
	public T value;
	
	public ConstantExpression(Port<T> port, T value)
	{
		super(port);
		
		this.value = value;
	}
	
	@Override
	public T evaluate(int timepoint)
	{
		return value;
	}

}
