package org.xtream.demo.projecthouse.model.thermalstorage.pelletheater;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Module;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.projecthouse.enums.OnOffDecision;

public class PelletHeaterModule extends Module {
	
	public PelletHeaterContext context = new PelletHeaterContext();
	public PelletHeaterController controller = new PelletHeaterController();
	
	public Chart cost = new Timeline(context.costOutput);
	
	public ChannelExpression<OnOffDecision> onOffChannel = new ChannelExpression<>(context.onOffInput, controller.onOffOutput);

}
