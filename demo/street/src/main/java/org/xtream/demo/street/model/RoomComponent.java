package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;

public class RoomComponent extends Component
{
	
	// Ports
	
	public Port<Double> loadOutput = new Port<>();
	
	// Components
	
	public NetComponent net = new NetComponent(1);
	public VolumeComponent volume = new VolumeComponent(1, 20);
	public FridgeComponent fridge = new FridgeComponent();
	
	// Expressions
	
	public Expression<Double> balance = new ChannelExpression<>(loadOutput, net.balanceOutput);
	public Expression<Double> load = new ChannelExpression<>(net.loadInputs[0], fridge.loadOutput); 

}
