package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;

public class ScenarioComponent extends Component
{
	
	// Ports
	
	public Port<Double> inflowOutput = new Port<>();
	public Port<Double> priceOutput = new Port<>();
	
	// Charts
	
	public Chart inflowChart = new Timeline(inflowOutput);
	public Chart priceChart = new Timeline(priceOutput);
	
	// Expressions
	
	public Expression<Double> inflowExpression = new Expression<Double>(inflowOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getInflow(0, Constants.START + timepoint);
		}
	};
	public Expression<Double> priceExpression = new Expression<Double>(priceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Constants.DATASET.getPrice(Constants.START + timepoint);
		}

	};

}
