package org.xtream.demo.mobile.model.overallsystems;

import org.xtream.core.datatypes.Edge;
import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.components.AbstractConstraintsComponent;

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
	
	Graph graph;
	
	// Inputs

	
	public Port<Double>[] positionTraversedLengthInputs;
	public Port<Double>[] vehicleLengthInputs;
	public Port<Edge>[] positionInputs;
	
	public Port<Double> productionInput = new Port<>();
	public Port<Double> consumptionInput = new Port<>();
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override public Boolean evaluate(int timepoint)
		{	
			
			for (int i = 0; i < positionInputs.length; i++)
			{
				// Vehicle 1 Position
				Port<Edge> position = positionInputs[i];
				
				int maximumAllowedVehicles = (int) graph.getEdgeWeight(position.get(timepoint).getName());
				
				for (int j = 0; j < positionInputs.length; j++)
				{	
					// Vehicle 2 Position
					Port<Edge> position2 = positionInputs[j];
					
					if (!(position.equals(position2)) && position.get(timepoint).equals(position2.get(timepoint)))
					{
						
						if (maximumAllowedVehicles <= 0)
						{
							return false;
						}
						
						if (Math.abs(positionTraversedLengthInputs[i].get(timepoint)-(positionTraversedLengthInputs[j].get(timepoint))) < (vehicleLengthInputs[i].get(timepoint)+vehicleLengthInputs[j].get(timepoint))) 
						{				
							maximumAllowedVehicles++;
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
