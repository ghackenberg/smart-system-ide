package org.xtream.demo.projecthouse.model.battery;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Module;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.ChargingDecision;
import org.xtream.demo.projecthouse.model.Consumer;
import org.xtream.demo.projecthouse.model.Producer;

public class BatteryModule extends Module implements Producer, Consumer {

	public BatteryContext context = new BatteryContext();
	public BatteryController controller = new BatteryController();
	
	public Chart balance = new Timeline(context.balanceOutput);
	public Chart soc = new Timeline(context.socOutput);

	public Expression<ChargingDecision> chargingChannel = new ChannelExpression<>(
			context.chargingInput, controller.chargingOutput);

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

	@Override
	public Port<Double> production() {
		// TODO Auto-generated method stub
		return context.productionOutput;
	}

}
