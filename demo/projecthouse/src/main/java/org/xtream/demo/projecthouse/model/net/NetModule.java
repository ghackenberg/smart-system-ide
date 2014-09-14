package org.xtream.demo.projecthouse.model.net;

import org.xtream.core.model.Chart;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Module;
import org.xtream.core.model.expressions.ChannelExpression;

public class NetModule extends Module{
	
	public NetContext context;
	
	public Chart constancy;
	public Chart cost;
	public Chart balance;
	
	public ChannelExpression<Double> powerExpression;
	
	public NetModule(String filename) {
		context = new NetContext(filename);
		constancy = new Timeline(context.constancyOutput);
		balance = new Timeline(context.balanceOutput);
	}

}
