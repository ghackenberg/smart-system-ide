package org.xtream.core.model.components;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public abstract class AbstractCostsComponent extends Component
{
	
	public AbstractCostsComponent()
	{
		super(AbstractCostsComponent.class.getClassLoader().getResource("costs.png"));
	}
	
	// Outputs
	
	public Port<Double> costsOutput = new Port<>();
	
	// Charts
	
	public Chart costsChart = new Chart(costsOutput);

}
