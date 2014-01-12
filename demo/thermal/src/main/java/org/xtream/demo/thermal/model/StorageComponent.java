package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.ports.OutputPort;

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
	
	public OutputPort<Double> command = new OutputPort<>();

	public OutputPort<Double> level = new OutputPort<>();
	
	public OutputPort<Double> minimum = new OutputPort<>();
	
	public OutputPort<Double> maximum = new OutputPort<>();
	
	@Constraint
	public OutputPort<Boolean> constraint = new OutputPort<>();
	
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
	
	public Expression<Double> commandExpression = new ConstantNonDeterministicExpression<Double>(command, new SetBuilder<Double>().add(-1.).add(-0.5).add(0.).add(0.5).add(1.));
	
	public Expression<Double> productionExpression = new Expression<Double>(production)
	{
		@Override public Double evaluate(int timepoint)
		{
			return command.get(timepoint) > 0. ? command.get(timepoint) * speed : 0.;
		}
	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumption)
	{
		@Override public Double evaluate(int timepoint)
		{
			return command.get(timepoint) < 0. ? command.get(timepoint) * speed : 0.;
		}
	};
	
	public Expression<Double> levelExpression = new Expression<Double>(level)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return capacity * 0.75;
			}
			else
			{
				if (command.get(timepoint) < 0.)
				{
					return level.get(timepoint - 1) * 0.99 - balance.get(timepoint) * 0.85; 
				}
				else if (command.get(timepoint) == 0.)
				{
					return level.get(timepoint - 1) * 0.99;
				}
				else if (command.get(timepoint) > 0.)
				{
					return level.get(timepoint - 1) * 0.99 - balance.get(timepoint);
				}
				
				throw new IllegalStateException();
			}
		}
	};
	
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimum, 0.);
	
	public Expression<Double> maximumExpression = new Expression<Double>(maximum)
	{
		@Override public Double evaluate(int timepoint)
		{
			return capacity;
		}
	};
	
	public Expression<Boolean> constraintExpression = new Expression<Boolean>(constraint)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return level.get(timepoint) >= minimum.get(timepoint) && level.get(timepoint) <= maximum.get(timepoint);
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	public Chart energyChart = new Chart(production, consumption, balance, level, minimum, maximum);

}
