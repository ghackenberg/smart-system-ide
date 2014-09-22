package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;

public class FloorComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public FloorComponent(int size)
	{
		net = new NetComponent(size);
		
		rooms = new RoomComponent[size];
		for (int i = 0; i < size; i++)
		{
			rooms[i] = new RoomComponent();
		}
		
		balance = new ChannelExpression<>(loadOutput, net.balanceOutput);
		
		loads = new Expression[size];
		for (int i = 0; i < size; i++)
		{
			loads[i] = new ChannelExpression<>(net.loadInputs[i], rooms[i].loadOutput);
		}
	}
	
	// Ports
	
	public Port<Double> loadOutput = new Port<>();
	
	// Components
	
	public NetComponent net;
	public RoomComponent[] rooms;
	
	// Expressions
	
	public Expression<Double> balance;
	public Expression<Double>[] loads;

}
