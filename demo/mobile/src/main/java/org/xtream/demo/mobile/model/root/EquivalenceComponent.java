package org.xtream.demo.mobile.model.root;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	@SuppressWarnings("unchecked")
	public EquivalenceComponent(ModulesContainer modules)
	{
		int modulesLength = modules.modules.length;
		chargeStateRelativeInputs = new Port[modulesLength];
		chargeStateRelative = new ChannelExpression[modulesLength];
		
		for (int i = 0; i < modulesLength; i++)
		{
			chargeStateRelativeInputs[i] = new Port<>();
			VehicleContainer vehicleModule = (VehicleContainer) modules.modules[i];
			chargeStateRelative[i] = new ChannelExpression<>(chargeStateRelativeInputs[i], vehicleModule.context.chargeStateRelativeOutput);
		}
	}
	
	// Inputs

	public Port<Double>[] chargeStateRelativeInputs;
	
	// Outputs
	
	public Port<Double> equivalenceOutput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double>[] chargeStateRelative;
	
	// Equivalences
	
	public Equivalence equivalence = new Equivalence(equivalenceOutput);
	
	// Expressions
	
	public Expression<Double> equivalenceExpression = new Expression<Double>(equivalenceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < chargeStateRelativeInputs.length; i++)
			{
				sum += chargeStateRelativeInputs[i].get(state, timepoint);
			}
			
			return sum;
		}
	};
	
}
