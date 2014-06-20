package org.xtream.demo.street.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Constraint;
import org.xtream.core.optimizer.State;

public class VolumeComponent extends Component
{
	
	// Constructors
	
	public VolumeComponent()
	{
		this.minimum = Double.MIN_VALUE;
		this.maximum = Double.MAX_VALUE;
		
		this.temperatureChart = new Timeline(temperatureOutput);
	}
	public VolumeComponent(double minimum, double maximum)
	{
		this.minimum = minimum;
		this.maximum = maximum;
		
		this.temperatureChart = new Timeline(minimumOutput, temperatureOutput, maximumOutput);
	}
	
	// Parameters
	
	protected double minimum;
	protected double maximum;
	
	// Ports
	
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
			// TODO Develop temperature formula.
			return 0.0;
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
