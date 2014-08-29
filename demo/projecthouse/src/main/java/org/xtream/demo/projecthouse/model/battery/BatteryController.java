package org.xtream.demo.projecthouse.model.battery;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.projecthouse.enums.ChargingDecision;

public class BatteryController extends Component {

	public Port<ChargingDecision> chargingOutput = new Port<>();

	public Expression<ChargingDecision> chargingExpression = new ConstantNonDeterministicExpression<>(
			chargingOutput, ChargingDecision.values());

}
