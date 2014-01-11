package org.xtream.demo.thermal.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Show;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.ports.OutputPort;

public class ThermalComponent extends EnergyComponent
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public OutputPort<Boolean> command = new OutputPort<>();

	@Show("Temperature")
	public OutputPort<Double> temperature = new OutputPort<>();
	
	@Show("Temperature")
	public OutputPort<Double> minimum = new OutputPort<>();
	
	@Show("Temperature")
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
	
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimum, 2.);
	
	public Expression<Double> maximumExpression = new ConstantExpression<Double>(maximum, 8.);
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumption)
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
	
	public Expression<Double> productionExpression = new ConstantExpression<Double>(production, 0.);
	
	public Expression<Boolean> constraintExpression = new Expression<Boolean>(constraint)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return temperature.get(timepoint) >= minimum.get(timepoint) && temperature.get(timepoint) < maximum.get(timepoint);
		}
	};

}
