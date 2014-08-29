package org.xtream.demo.projecthouse.model.thermalstorage.heatpump;

import org.xtream.core.model.Port;
import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.model.Consumer;

public class HeatPumpModule extends Module implements Consumer {
	
	public HeatPumpContext context = new HeatPumpContext();
	public HeatPumpController controller = new HeatPumpController();
	
	public ChannelExpression<Integer> levelChannel = new ChannelExpression<>(context.levelInput, controller.levelOutput);

	@Override
	public Port<Double> consumption() {
		return context.consumptionOutput;
	}

}
