package org.xtream.core.model.components;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ChannelExpression;

public abstract class AbstractModulesComponent extends Component
{
	
	@SuppressWarnings("unchecked")
	public AbstractModulesComponent()
	{
		super(AbstractModulesComponent.class.getClassLoader().getResource("modules.png"));
		
		modules = new AbstractModuleComponent[0];
		costsOutputs = new Port[0];
		costs = new ChannelExpression[0];
		modulePreviews = new Chart[0];
	}
	
	@SuppressWarnings("unchecked")
	public AbstractModulesComponent(AbstractModuleComponent<?, ?, ?, ?, ?, ?>[] modules)
	{
		super(AbstractModulesComponent.class.getClassLoader().getResource("modules.png"));
		
		this.modules = modules;
		
		costsOutputs = new Port[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			costsOutputs[i] = new Port<>();
		}
		
		costs = new ChannelExpression[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			costs[i] = new ChannelExpression<>(costsOutputs[i], modules[i].costsOutput);
		}
		
		modulePreviews = new Chart[modules.length];
		for (int i = 0; i < modules.length; i++)
		{
			modulePreviews[i] = new Chart(modules[i].modulePreview.ports);
		}
	}
	
	// Outputs
	
	public Port<Double> costsOutputs[];
	
	// Components
	
	public AbstractModuleComponent<?, ?, ?, ?, ?, ?>[] modules;
	
	// Channels
	
	public ChannelExpression<Double> costs[];
	
	// Previews
	
	public Chart modulePreviews[];

}
