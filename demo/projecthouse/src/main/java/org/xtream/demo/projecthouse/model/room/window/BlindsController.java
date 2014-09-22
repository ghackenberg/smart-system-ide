package org.xtream.demo.projecthouse.model.room.window;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.demo.projecthouse.expressions.RandomExpression;

public class BlindsController extends Component {
	
	public Port<Double> blindsOutput = new Port<Double>();
	
	public Expression<Double> blindsExpression = new RandomExpression(blindsOutput); //TODO [Andreas] Maybe also use sun?
	
	public Chart blindsChart = new Timeline(blindsOutput);

}
