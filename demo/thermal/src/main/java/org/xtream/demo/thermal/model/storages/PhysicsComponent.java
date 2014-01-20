package org.xtream.demo.thermal.model.storages;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.thermal.model.commons.EnergyPhysicsComponent;

public class PhysicsComponent extends EnergyPhysicsComponent
{
	
	public PhysicsComponent(double speed, double capacity)
	{
		this.speed = speed;
		this.capacity = capacity;
	}
	
	// Parameters
	
	private double speed;
	private double capacity;
	
	// Inputs
	
	public Port<Double> commandInput = new Port<>();
	
	// Outputs
	
	public Port<Double> levelOutput = new Port<>();
	public Port<Double> minimumOutput = new Port<>();
	public Port<Double> maximumOutput = new Port<>();
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandInput.get(timepoint) > 0. ? commandInput.get(timepoint) * speed : 0.;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandInput.get(timepoint) < 0. ? commandInput.get(timepoint) * speed : 0.;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return capacity * 0.5;
			}
			else
			{
				if (commandInput.get(timepoint) < 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99 - balanceOutput.get(timepoint) * 0.85; 
				}
				else if (commandInput.get(timepoint) == 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99;
				}
				else if (commandInput.get(timepoint) > 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99 - balanceOutput.get(timepoint);
				}
				
				throw new IllegalStateException();
			}
		}
	};
	public Expression<Double> maximumExpression = new Expression<Double>(maximumOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return capacity;
		}
	};
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimumOutput, 0.);

}
