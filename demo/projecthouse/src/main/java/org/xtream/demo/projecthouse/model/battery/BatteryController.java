package org.xtream.demo.projecthouse.model.battery;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.projecthouse.enums.ChargingDecision;

public class BatteryController extends Component {
	
	public Port<Double> socInput = new Port<>();

	public Port<ChargingDecision> chargingOutput = new Port<>();

	public Expression<ChargingDecision> chargingExpression = new ConstantNonDeterministicExpression<>(
			chargingOutput, ChargingDecision.values());

}
