package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.optimizer.State;

public class FridgeComponent extends Component
{
	
	// Ports
	
	public Port<Double> loadOutput = new Port<>();
	
	// Components
	
	public VolumeComponent hotVolume = new VolumeComponent();
	public VolumeComponent coldVolume = new VolumeComponent(2, 8);
	
	// Expressions
	
	public Expression<Double> loadExpression = new Expression<Double>(loadOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
		
	};

}
