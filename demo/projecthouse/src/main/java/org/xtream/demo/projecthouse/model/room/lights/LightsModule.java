package org.xtream.demo.projecthouse.model.room.lights;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Consumer;

public class LightsModule extends Module implements Consumer{
	
	public LightsContext context;
	public LightsController controller = new LightsController();
	
	public ChannelExpression<OnOffDecision> onOffChannel;
	
	public LightsModule(double consumption) {
		super();
		context = new LightsContext(consumption);
		onOffChannel = new ChannelExpression<>(context.onOffInput, controller.onOffOutput);
	}

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

}
