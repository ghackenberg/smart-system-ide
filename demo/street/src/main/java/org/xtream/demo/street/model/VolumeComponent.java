package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Constraint;

public class VolumeComponent extends Component
{
	
	// Constructors
	
	public VolumeComponent(int size, double initial)
	{
		this(size, initial, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	@SuppressWarnings("unchecked")
	public VolumeComponent(int size, double initial, double minimum, double maximum)
	{
		this.initial = initial;
		this.minimum = minimum;
		this.maximum = maximum;
		
		heatInputs = new Port[size];
		for (int i = 0; i < size; i++)
		{
			heatInputs[i] = new Port<>();
		}

		if (minimum == Double.MIN_VALUE && maximum == Double.MAX_VALUE)
		{
			this.temperatureChart = new Timeline(temperatureOutput);
		}
		else
		{
			this.temperatureChart = new Timeline(minimumOutput, temperatureOutput, maximumOutput);
		}
	}
	
	// Parameters
	
	protected double initial;
	protected double minimum;
	protected double maximum;
	
	// Ports
	
	public Port<Double>[] heatInputs;
	
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> minimumOutput = new Port<>();
	public Port<Double> maximumOutput = new Port<>();
	public Port<Boolean> validOutput = new Port<>();
	
	// Markers
	
	public Constraint validConstraint = new Constraint(validOutput);
	
	// Charts
	
	public Chart temperatureChart;
	
	// Expressions
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return initial;
			}
			else
			{
				double temperature = temperatureExpression.get(state, timepoint - 1);
				
				for (Port<Double> heatInput : heatInputs)
				{
					temperature += heatInput.get(state, timepoint);
				}
				
				return temperature;
			}
		}
	};
	public Expression<Double> minimumExpression = new Expression<Double>(minimumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return minimum;
		}
	};
	public Expression<Double> maximumExpression = new Expression<Double>(maximumOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return maximum;
		}
	};
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return temperatureOutput.get(state, timepoint) >= minimumOutput.get(state, timepoint) && temperatureOutput.get(state, timepoint) <= maximumOutput.get(state, timepoint);
		}
	};

}
