package org.xtream.demo.thermal.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class StorageComponent extends EnergyComponent
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public OutputPort<Double> command = new OutputPort<>();

	@Show("Main")
	public OutputPort<Double> level = new OutputPort<>();
	
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
			return command.get(timepoint) > 0. ? command.get(timepoint) * 800. : 0.;
		}
	};
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumption)
	{
		@Override public Double evaluate(int timepoint)
		{
			return command.get(timepoint) < 0. ? command.get(timepoint) * 800. : 0.;
		}
	};
	
	public Expression<Double> levelExpression = new Expression<Double>(level)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 7500.;
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
	
	public Expression<Boolean> constraintExpression = new Expression<Boolean>(constraint)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return level.get(timepoint) >= 0. && level.get(timepoint) <= 10000.;
		}
	};

}
