package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;

public class HouseholdComponent extends Component
{
	
	// Ports
	
	public Port<Double> load = new Port<Double>();
	
	// Charts
	
	public Timeline loadChart = new Timeline(load);
	
	// Expressions
	
	public Expression<Double> loadExpr = new Expression<Double>(load)
	{
		@Override public Double evaluate(int timepoint)
		{
			return 1.0;
		}
	};

}
