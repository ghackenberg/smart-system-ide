package org.xtream.demo.projecthouse.model.net;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.model.Consumer;
import org.xtream.demo.projecthouse.model.Producer;

public class NetModule extends Module implements Consumer, Producer{
	
	public NetContext context;
	public NetController controller = new NetController();
	
	public ChannelExpression<Double> powerExpression;
	
	public NetModule(String filename) {
		context = new NetContext(filename);
		powerExpression = new ChannelExpression<>(context.powerInput, controller.powerOutput);
	}

	@Override
	public Port<Double> production() {
		return context.productionOutput;
	}

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

}
