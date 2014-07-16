package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;

public class BuildingComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public BuildingComponent(int size)
	{
		net = new NetComponent(size);
		
		floors = new FloorComponent[size];
		for (int i = 0; i < size; i++)
		{
			floors[i] = new FloorComponent(2);
		}
		
		balance = new ChannelExpression<>(loadOutput, net.balanceOutput);
		
		loads = new Expression[size];
		for (int i = 0; i < size; i++)
		{
			loads[i] = new ChannelExpression<>(net.loadInputs[i], floors[i].loadOutput);
		}
		
		balanceChart = new Timeline(net.balanceOutput);
	}
	
	// Ports
	
	public Port<Double> loadOutput = new Port<Double>();
	
	// Components
	
	public NetComponent net;
	public FloorComponent[] floors;
	
	// Charts
	
	public Chart balanceChart;
	
	// Expressions
	
	public Expression<Double> balance;
	public Expression<Double>[] loads;

}
