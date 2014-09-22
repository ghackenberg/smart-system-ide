package org.xtream.demo.projecthouse.model.thermalstorage.heatpump;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class HeatPumpController extends Component {

	public Port<Integer> levelOutput = new Port<>();

	public Expression<Integer> levelExpression = new ConstantNonDeterministicExpression<>(
			levelOutput, new Integer[] { 0, 1, 2 });

}
