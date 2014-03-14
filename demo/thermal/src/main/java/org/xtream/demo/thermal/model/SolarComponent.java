package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;

public abstract class SolarComponent extends EnergyComponent
{
	
	public SolarComponent(double scale)
	{
		super(SolarComponent.class.getClassLoader().getResource("producer.png"));
		
		this.scale = scale;
	}
	
	// Parameters
	
	protected double scale;
	
	// Inputs
	
	public Port<Double> efficiencyInput = new Port<>();
	
	// Charts
	
	public Chart energyChart = new Chart(productionOutput);
	
	// Expressions
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			int modulo = timepoint % 96;
			
			double mean = 12 * 4;
			double variance = 3 * 4;
			double difference = modulo - mean;
			
			/*
			if (modulo < (mean - variance) * 4)
			{
				return 0.;
			}
			else if (modulo > (mean + variance) * 4)
			{
				return 0.;
			}
			else
			{
				double difference = (modulo - mean * 4) / (variance * 4);
				
				return (1 - difference * difference) * scale;
			}
			*/
			
			return Math.exp(-difference/variance*difference/variance) * scale;
		}
	};
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);

}
