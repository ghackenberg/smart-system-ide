package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.expressions.ConstantExpression;

public abstract class StorageComponent extends EnergyComponent
{

	public StorageComponent(double speed, double capacity)
	{
		super(StorageComponent.class.getClassLoader().getResource("buffer.png"));
		
		this.speed = speed;
		this.capacity = capacity;
	}
	
	// Parameters
	
	protected double speed;
	protected double capacity;
	protected double efficiency = 0.5;
	protected double loss = 0.99;
	
	// Inputs
	
	public Port<Double> commandInput = new Port<>();
	
	// Outputs
	
	public Port<Double> levelOutput = new Port<>();
	public Port<Double> minimumOutput = new Port<>();
	public Port<Double> maximumOutput = new Port<>();
	public Port<Boolean> validOutput = new Port<>();
	
	// Constraints
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Charts
	
	public Chart energyChart = new Chart(minimumOutput, levelOutput, maximumOutput);
	
	// Previews
	
	public Chart energyPreview = new Chart(minimumOutput, levelOutput, maximumOutput);
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandInput.get(timepoint) > 0. ? speed : 0.;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandInput.get(timepoint) < 0. ? -speed : 0.;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.5 * capacity;
			}
			else
			{
				if (commandInput.get(timepoint) < 0.)
				{
					return levelOutput.get(timepoint - 1) * loss + speed * efficiency; 
				}
				else if (commandInput.get(timepoint) == 0.)
				{
					return levelOutput.get(timepoint - 1) * loss;
				}
				else if (commandInput.get(timepoint) > 0.)
				{
					return levelOutput.get(timepoint - 1) * loss - speed;
				}
				
				throw new IllegalStateException();
			}
		}
	};
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return levelOutput.get(timepoint) >= minimumOutput.get(timepoint) && levelOutput.get(timepoint) <= maximumOutput.get(timepoint);
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
