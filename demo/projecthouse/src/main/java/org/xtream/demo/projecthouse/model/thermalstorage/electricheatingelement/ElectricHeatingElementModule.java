package org.xtream.demo.projecthouse.model.thermalstorage.electricheatingelement;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Module;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;
import org.xtream.demo.projecthouse.model.Consumer;

public class ElectricHeatingElementModule extends Module implements Consumer {
	
	public ElectricHeatingElementController controller = new ElectricHeatingElementController();
	public ElectricHeatingElementContext context = new ElectricHeatingElementContext();
	
	public Chart consumption = new Timeline(consumption());
	
	public ChannelExpression<OnOffDecision> onOffChannel = new ChannelExpression<>(context.onOffInput, controller.onOffOutput);

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

}
