package org.xtream.demo.hydro.model.control.reactive.single.discrete;

import java.util.Set;

import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class DischargeExpression extends ConstantNonDeterministicExpression<Double>
{
	
	public DischargeExpression(Port<Double> port, Double[] set)
	{
		super(port, set);
	}
	public DischargeExpression(Port<Double> port, Set<Double> set)
	{
		super(port, set);
	}

}
