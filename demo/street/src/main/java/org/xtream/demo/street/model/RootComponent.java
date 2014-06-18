package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{

	public static void main(String[] args)
	{
		new Workbench<>(new RootComponent(), 96, 50, 25, 0, 0);
	}
	
	// Components
	
	public NetComponent net = new NetComponent();
	public HouseholdComponent household_00 = new HouseholdComponent();
	public HouseholdComponent household_01 = new HouseholdComponent();

	// Charts
	
	public Chart costChart = new Timeline(net.balance); // TODO Cost output port
	
	// Expressions
	
	public Expression<Double> load_00 = new ChannelExpression<>(net.load_00, household_00.load);
	public Expression<Double> load_01 = new ChannelExpression<>(net.load_01, household_01.load);
	
}
