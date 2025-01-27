package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{

	public static void main(String[] args)
	{
		new Workbench<>(new Engine<>(new RootComponent(2), 100, 100, 2, 5), 96);
	}
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public RootComponent(int size)
	{
		net = new NetComponent(size);
		
		buildings = new BuildingComponent[size];
		for (int i = 0; i < size; i++)
		{
			buildings[i] = new BuildingComponent(2);
		}
		
		loads = new Expression[size];
		for (int i = 0; i < size; i++)
		{
			loads[i] = new ChannelExpression<>(net.loadInputs[i], buildings[i].loadOutput);
		}
		
		balanceChart = new Timeline(net.balanceOutput);
	}
	
	// Components

	public NetComponent net;
	public BuildingComponent[] buildings;

	// Charts
	
	public Chart balanceChart;
	
	// Expressions
	
	public Expression<Double>[] loads;
	
}
