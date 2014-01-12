package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.builders.SetBuilder;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;

public class ThermalComponent extends EnergyComponent
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Boolean> commandOutput = new Port<>();
	
	public Port<Double> temperatureOutput = new Port<>();
	
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
	
	public Expression<Boolean> commandExpression = new ConstantNonDeterministicExpression<Boolean>(commandOutput, new SetBuilder<Boolean>().add(true).add(false));

	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (timepoint == 0)
			{
				return 5.;
			}
			else
			{
				if (commandOutput.get(timepoint))
				{
					return temperatureOutput.get(timepoint - 1) - 0.1;
				}
				else
				{
					return temperatureOutput.get(timepoint - 1) + 0.1;
				}
			}
		}
	};
	
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimumOutput, 2.);
	
	public Expression<Double> maximumExpression = new ConstantExpression<Double>(maximumOutput, 8.);
	
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			if (commandOutput.get(timepoint))
			{
				return -200.;
			}
			else
			{
				return 0.;
			}
		}
	};
	
	public Expression<Double> productionExpression = new ConstantExpression<Double>(productionOutput, 0.);
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override public Boolean evaluate(int timepoint)
		{
			return temperatureOutput.get(timepoint) >= minimumOutput.get(timepoint) && temperatureOutput.get(timepoint) < maximumOutput.get(timepoint);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	public Constraint levelConstraint = new Constraint(validOutput);
	
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
	
	public Chart temperatureChart = new Chart(temperatureOutput, minimumOutput, maximumOutput);

}
