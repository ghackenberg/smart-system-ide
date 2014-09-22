package org.xtream.demo.mobile.model.root;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;

public class ObjectiveComponent extends Component
{
	@SuppressWarnings("unchecked")
	public ObjectiveComponent(ModulesContainer modules)
	{
		int modulesLength = modules.modules.length;
		costInputs = new Port[modulesLength];
		cost = new ChannelExpression[modulesLength];
			
		
		for (int i = 0; i < modules.modules.length; i++)
		{
			costInputs[i] = new Port<>();
			VehicleContainer vehicleModule = (VehicleContainer) modules.modules[i];
			cost[i] = new ChannelExpression<>(costInputs[i], vehicleModule.objectives.costsOutput);
		}
	}
	
	// Inputs

	public Port<Double>[] costInputs;
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double>[] cost;
	
	
	// Objectives
	
	public Objective costObjective = new MinObjective(costOutput);

	// Expressions
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < costInputs.length; i++)
			{
				sum += costInputs[i].get(state, timepoint);
			}
			
			return ((timepoint == 0 ? 0 : costOutput.get(state, timepoint - 1)) + sum);
		}
	};
	
}
