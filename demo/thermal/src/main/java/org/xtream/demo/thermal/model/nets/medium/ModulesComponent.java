package org.xtream.demo.thermal.model.nets.medium;

import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.thermal.model.LowNetComponent;


public class ModulesComponent extends org.xtream.demo.thermal.model.nets.ModulesComponent
{
	
	@SuppressWarnings("unchecked")
	public ModulesComponent(LowNetComponent... nets)
	{
		super(nets.length);
		
		// Net components
		
		this.nets = nets;
		
		// Balance channels
		
		balances = new ChannelExpression[nets.length];
		
		for (int i = 0; i < nets.length; i++)
		{
			balances[i] = new ChannelExpression<>(balanceOutputs[i], nets[i].balanceOutput);
		}
	}
	
	// Components
	
	public LowNetComponent[] nets;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
}
