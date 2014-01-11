package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class StorageComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public OutputPort<Integer> command = new OutputPort<>();
	
	@Show("Main")
	public OutputPort<Double> energy = new OutputPort<>();

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
	
	public Expression<Integer> commandExpression = new ConstantNonDeterministicExpression<Integer>(command, new SetBuilder<Integer>().add(-1).add(0).add(1));
	
	public Expression<Double> energyExpression = new Expression<Double>(energy)
	{
		@Override public Double evaluate(int timepoint)
		{
			return command.get(timepoint) * 600.;
		}
	};
	
	public Expression<Double> levelExpression = new Expression<Double>(level)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 5000.;
			}
			else
			{
				if (command.get(timepoint) == -1)
				{
					return level.get(timepoint - 1) * 0.99 - energy.get(timepoint) * 0.85; 
				}
				else if (command.get(timepoint) == 0)
				{
					return level.get(timepoint - 1) * 0.99;
				}
				else if (command.get(timepoint) == 1)
				{
					return level.get(timepoint - 1) * 0.99 - energy.get(timepoint);
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
