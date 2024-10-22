package org.xtream.demo.thermal.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Constraint;

public class NetComponent extends EnergyComponent
{
	
	@SuppressWarnings("unchecked")
	public NetComponent(int size)
	{
		super(NetComponent.class.getClassLoader().getResource("elements/net.png"));
		
		this.capacity = Double.MAX_VALUE;
		
		// Balance channels
		
		balanceInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			balanceInputs[i] = new Port<>();
		}
	}
	
	// Parameters
	
	protected double capacity;
	
	// Inputs
	
	public Port<Double>[] balanceInputs;
	
	// Outputs
	
	public Port<Boolean> validOutput = new Port<>();
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double production = 0;
			
			for (Port<Double> terminal : balanceInputs)
			{
				double current = terminal.get(state, timepoint);
				
				production += current > 0. ? current : 0.;
			}
			
			return production;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double consumption = 0;
			
			for (Port<Double> terminal : balanceInputs)
			{
				double current = terminal.get(state, timepoint);
				
				consumption += current < 0. ? current : 0.;
			}
			
			return consumption;
		}
	};
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{	
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return productionOutput.get(state, timepoint) >= -capacity && consumptionOutput.get(state, timepoint) <= capacity; 
		}
	};
	
}
