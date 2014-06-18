package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractConstraintsComponent;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.optimizer.State;

public class ConstraintsComponent extends AbstractConstraintsComponent
{
	
	@SuppressWarnings("unchecked")
	public ConstraintsComponent(int size, Graph graph)
	{
		this.graph = graph;
		
		positionTraversedLengthInputs = new Port[size];
		positionInputs = new Port[size];
		vehicleLengthInputs = new Port[size];
		
		
		for (int i = 0; i < size; i++)
		{
			
			positionTraversedLengthInputs[i] = new Port<>();
			positionInputs[i] = new Port<>();
			vehicleLengthInputs[i] = new Port<>();
			
		}
	}
	
	// Parameters
	
	protected Graph graph;
	
	// Inputs

	
	public Port<Double>[] positionTraversedLengthInputs;
	public Port<Double>[] vehicleLengthInputs;
	public Port<Edge>[] positionInputs;
	
	public Port<Double> productionInput = new Port<>();
	public Port<Double> consumptionInput = new Port<>();
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput, true)
	{	
		@Override protected Boolean evaluate(State state, int timepoint)
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
						
						if (Math.abs(positionTraversedLengthInputs[i].get(state, timepoint)-(positionTraversedLengthInputs[j].get(state, timepoint))) < (vehicleLengthInputs[i].get(state, timepoint) + vehicleLengthInputs[j].get(state, timepoint))) 
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
