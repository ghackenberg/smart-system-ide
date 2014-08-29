package org.xtream.demo.projecthouse.expressions;

import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;

public class RandomOnOffExpression extends
		ConstantNonDeterministicExpression<OnOffDecision> {

	public RandomOnOffExpression(Port<OnOffDecision> port) {
		super(port, OnOffDecision.values());
	}

}
