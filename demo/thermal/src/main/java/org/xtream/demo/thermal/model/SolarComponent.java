package org.xtream.demo.thermal.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;

import au.com.bytecode.opencsv.CSVReader;

public abstract class SolarComponent extends EnergyComponent
{
	
	public SolarComponent(double scale)
	{
		super(SolarComponent.class.getClassLoader().getResource("producer.png"));
		
		this.scale = scale;
		
		try
		{
			CSVReader reader = new CSVReader(new FileReader("Scenario.csv"), ';');
			
			scenario = reader.readAll();
			
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	// Parameters
	
	protected double scale;
	protected List<String[]> scenario;
	
	// Inputs
	
	public Port<Double> efficiencyInput = new Port<>();
	
	// Charts
	
	public Chart energyChart = new Chart(productionOutput);
	
	// Expressions
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			try
			{
				return NumberFormat.getInstance().parse(scenario.get(timepoint + 1)[1]).doubleValue() * scale * efficiencyInput.get(timepoint);
			}
			catch (ParseException e)
			{
				throw new IllegalStateException(e);
			}
		}
	};
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);

}
