package org.xtream.demo.street.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;

public class InterfaceComponent extends Component
{
	
	// Constructors
	
	public InterfaceComponent(double conductivity, double thickness, double area)
	{
		this.conductivity = conductivity;
		this.thickness = thickness;
		this.area = area;
	}
	
	// Parameters
	
	protected double conductivity;
	protected double thickness;
	protected double area;
	
	// Ports
	
	public Port<Double> temperatureOneInput = new Port<>();
	public Port<Double> temperatureTwoInput = new Port<>();

	public Port<Double> heatOneOutput = new Port<>();
	public Port<Double> heatTwoOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> headOneExpression = new Expression<Double>(heatOneOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return - conductivity * area * (temperatureOneInput.get(state, timepoint) - temperatureTwoInput.get(state, timepoint)) / thickness;
		}
	};
	public Expression<Double> headTwoExpression = new Expression<Double>(heatTwoOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return - conductivity * area * (temperatureTwoInput.get(state, timepoint) - temperatureOneInput.get(state, timepoint)) / thickness;
		}
	};

}
