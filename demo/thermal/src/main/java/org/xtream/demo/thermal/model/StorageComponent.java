package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class StorageComponent extends EnergyComponent
{
	
	private double speed;
	private double capacity;
	
	public StorageComponent(double speed, double capacity)
	{
		this.speed = speed;
		this.capacity = capacity;
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> commandOutput = new Port<>();
	
	public Port<Double> levelOutput = new Port<>();
	
	public Port<Double> minimumOutput = new Port<>();
	
	public Port<Double> maximumOutput = new Port<>();
	
	public Port<Boolean> validOutput = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////

	/* none */
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> commandExpression = new ConstantNonDeterministicExpression<Double>(commandOutput, new SetBuilder<Double>().add(-1.).add(0.).add(1.));
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandOutput.get(timepoint) > 0. ? commandOutput.get(timepoint) * speed : 0.;
		}
	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return commandOutput.get(timepoint) < 0. ? commandOutput.get(timepoint) * speed : 0.;
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
				if (commandOutput.get(timepoint) < 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99 - balanceOutput.get(timepoint) * 0.85; 
				}
				else if (commandOutput.get(timepoint) == 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99;
				}
				else if (commandOutput.get(timepoint) > 0.)
				{
					return levelOutput.get(timepoint - 1) * 0.99 - balanceOutput.get(timepoint);
				}
				
				throw new IllegalStateException();
			}
		}
	};
	
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimumOutput, 0.);
	
	public Expression<Double> maximumExpression = new Expression<Double>(maximumOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return capacity;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return levelOutput.get(timepoint) >= minimumOutput.get(timepoint) && levelOutput.get(timepoint) <= maximumOutput.get(timepoint);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	/* none */
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	/* none */
	
	////////////
	// CHARTS //
	////////////
	
	public Chart levelChart = new Chart(levelOutput, minimumOutput, maximumOutput);
	
	//////////////
	// PREVIEWS //
	//////////////
	
	public Chart levelPreview = new Chart(levelOutput, minimumOutput, maximumOutput);

}
