package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ThermalComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public OutputPort<Boolean> command = new OutputPort<>();

	@Show("Main")
	public OutputPort<Double> temperature = new OutputPort<>();

	public OutputPort<Double> energy = new OutputPort<>();
	
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
	
	public Expression<Boolean> commandExpression = new ConstantNonDeterministicExpression<Boolean>(command, new SetBuilder<Boolean>().add(true).add(false));

	public Expression<Double> temperatureExpression = new Expression<Double>(temperature)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 5.;
			}
			else
			{
				if (command.get(timepoint))
				{
					return temperature.get(timepoint - 1) - 0.1;
				}
				else
				{
					return temperature.get(timepoint - 1) + 0.1;
				}
			}
		}
	};
	
	public Expression<Double> energyExpression = new Expression<Double>(energy)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (command.get(timepoint))
			{
				return -200.;
			}
			else
			{
				return 0.;
			}
		}
	};
	
	public Expression<Boolean> constraintExpression = new Expression<Boolean>(constraint)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return temperature.get(timepoint) > 2. && temperature.get(timepoint) < 8.;
		}
	};

}
