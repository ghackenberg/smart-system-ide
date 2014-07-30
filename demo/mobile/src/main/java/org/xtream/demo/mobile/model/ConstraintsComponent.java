package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.markers.Constraint;

public class ConstraintsComponent extends Component
{
	@SuppressWarnings("unchecked")
	public ConstraintsComponent(int size, Graph graph, ModulesContainer modules)
	{
		int modulesLength = modules.modules.length;
		this.graph = graph;
		
		positionTraversedLengthInputs = new Port[size];
		positionInputs = new Port[size];
		vehicleLengthInputs = new Port[size];
		positionTraversedLength = new ChannelExpression[modulesLength];
		position = new ChannelExpression[modulesLength];
		vehicleLength = new ChannelExpression[modulesLength];
		
		
		for (int i = 0; i < size; i++)
		{
			positionTraversedLengthInputs[i] = new Port<>();
			positionInputs[i] = new Port<>();
			vehicleLengthInputs[i] = new Port<>();
		}
		
		for (int i = 0; i < modules.modules.length; i++)
		{
			VehicleContainer vehicleModule = (VehicleContainer) modules.modules[i];
			positionTraversedLength[i] = new ChannelExpression<>(positionTraversedLengthInputs[i], vehicleModule.positionTraversedLengthOutput);
			position[i] = new ChannelExpression<>(positionInputs[i], vehicleModule.positionOutput);
			vehicleLength[i] = new ChannelExpression<>(vehicleLengthInputs[i], vehicleModule.vehicleLengthOutput);
		}
	}
	
	// Parameters
	
	public Graph graph;
	
	// Inputs

	public Port<Double>[] positionTraversedLengthInputs;
	public Port<Double>[] vehicleLengthInputs;
	public Port<Edge>[] positionInputs;
	
	public Port<Double> productionInput = new Port<>();
	public Port<Double> consumptionInput = new Port<>();
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Channels
	public ChannelExpression<Double>[] positionTraversedLength;
	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] vehicleLength;
	
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override public Boolean evaluate(State state, int timepoint)
		{	
			
			for (int i = 0; i < positionInputs.length; i++)
			{
				// Vehicle 1 Position
				Port<Edge> position = positionInputs[i];
	
				int maximumAllowedVehicles = (int) graph.getEdgeWeight(position.get(state, timepoint).getName());
				
				for (int j = 0; j < positionInputs.length; j++)
				{	
					// Vehicle 2 Position
					Port<Edge> position2 = positionInputs[j];
					
					if (!(position.equals(position2)) && position.get(state, timepoint).equals(position2.get(state, timepoint)))
					{
						
						if (maximumAllowedVehicles <= 0)
						{
							return false;
						}
						
						if (Math.abs(positionTraversedLengthInputs[i].get(state, timepoint)-(positionTraversedLengthInputs[j].get(state, timepoint))) < (vehicleLengthInputs[i].get(state, timepoint)+vehicleLengthInputs[j].get(state, timepoint))) 
						{				
							maximumAllowedVehicles--;
						}	
					
					}
	
				}
				
			}

			return true;
		}
	};
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);
}
