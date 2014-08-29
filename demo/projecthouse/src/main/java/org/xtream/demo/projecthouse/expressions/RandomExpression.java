package org.xtream.demo.projecthouse.expressions;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class RandomExpression extends Expression<Double> {

	public RandomExpression(Port<Double> port) {
		super(port);
	}

	@Override
	protected Double evaluate(State state, int timepoint) {
		return Math.random();
	}

}
