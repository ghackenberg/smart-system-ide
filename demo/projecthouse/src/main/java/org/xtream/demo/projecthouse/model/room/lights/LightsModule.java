package org.xtream.demo.projecthouse.model.room.lights;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Module;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Consumer;

public class LightsModule extends Module implements Consumer{
	
	public LightsContext context;
	public LightsController controller = new LightsController();
	
	public ChannelExpression<OnOffDecision> onOffChannel;
	
	public Chart consumption;
	
	public LightsModule(double consumption) {
		super();
		context = new LightsContext(consumption);
		onOffChannel = new ChannelExpression<>(context.onOffInput, controller.onOffOutput);
		this.consumption = new Timeline(consumption());
	}

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

}
