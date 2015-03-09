package org.xtream.demo.infrastructure.model.power;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.demo.infrastructure.model.EnergyComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;

public class SolarPanelComponent extends EnergyComponent
{

    // Parameters

    public Double powerScale; // in kwH
    
	public SolarPanelComponent(Double powerScale)
	{
		this.powerScale = powerScale;
	}
	
	// Inputs
	
	public Port<Double> efficiencyOutput = new Port<>();

	// Expressions
	
	public Expression<Double> efficiencyExpression = new ConstantNonDeterministicExpression<Double>(efficiencyOutput, 0., .5, 1.);
	
	public Expression<Double> powerOutputExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			int modulo = timepoint % Scenario.DURATION;
			
			double mean = Scenario.DURATION/2;
			double variance = 3 * (Scenario.DURATION/24);
			double difference = modulo - mean;
			
			return Math.exp(-difference/variance*difference/variance) * powerScale * efficiencyOutput.get(state, timepoint);
		}
	};
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return true;
		}
	};
	
	// Charts
	
	public Chart levelChart = new Timeline(powerOutput);
}
