package org.xtream.demo.thermal.model.solars;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.thermal.model.commons.EnergyPhysicsComponent;

import au.com.bytecode.opencsv.CSVReader;

public class PhysicsComponent extends EnergyPhysicsComponent
{
	
	public PhysicsComponent(double scale)
	{
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
	
	private double scale;
	private List<String[]> scenario;
	
	// Inputs
	
	public Port<Double> damingInput = new Port<>();
	
	// Expressions
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			try
			{
				return NumberFormat.getInstance().parse(scenario.get(timepoint + 1)[1]).doubleValue() * scale * damingInput.get(timepoint);
			}
			catch (ParseException e)
			{
				throw new IllegalStateException(e);
			}
		}
	};
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);

}
