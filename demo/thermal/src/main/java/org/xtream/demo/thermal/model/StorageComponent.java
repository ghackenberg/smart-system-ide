package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.markers.Constraint;

public abstract class StorageComponent extends EnergyComponent
{

	public StorageComponent(double speed, double capacity)
	{
		super(StorageComponent.class.getClassLoader().getResource("elements/storage.png"));
		
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
	
	public Chart levelChart = new Timeline(minimumOutput, levelOutput, maximumOutput);
	
	// Expressions

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return commandInput.get(state, timepoint) > 0. ? speed : 0.;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return commandInput.get(state, timepoint) < 0. ? -speed : 0.;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.5 * capacity;
			}
			else
			{
				if (commandInput.get(state, timepoint) < 0.)
				{
					return levelOutput.get(state, timepoint - 1) * loss + speed * efficiency; 
				}
				else if (commandInput.get(state, timepoint) == 0.)
				{
					return levelOutput.get(state, timepoint - 1) * loss;
				}
				else if (commandInput.get(state, timepoint) > 0.)
				{
					return levelOutput.get(state, timepoint - 1) * loss - speed;
				}
				
				throw new IllegalStateException();
			}
		}
	};
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return levelOutput.get(state, timepoint) >= minimumOutput.get(state, timepoint) && levelOutput.get(state, timepoint) <= maximumOutput.get(state, timepoint);
		}
	};
	public Expression<Double> maximumExpression = new Expression<Double>(maximumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return capacity;
		}
	};
	public Expression<Double> minimumExpression = new ConstantExpression<Double>(minimumOutput, 0.);

}
