package org.xtream.demo.thermal.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.xtream.core.model.Expression;
import org.xtream.core.model.expressions.ConstantExpression;

import au.com.bytecode.opencsv.CSVReader;

public class SolarComponent extends EnergyComponent
{

	private double scale;
	private List<String[]> scenario;
	
	public SolarComponent(double scale)
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
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	/* none */
	
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
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			try
			{
				return NumberFormat.getInstance().parse(scenario.get(timepoint + 1)[1]).doubleValue() * scale;
			}
			catch (ParseException e)
			{
				throw new IllegalStateException(e);
			}
		}
	};
	
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumptionOutput, 0.);
	
	public Expression<Double> temperatureExpression = new ConstantExpression<Double>(temperatureOutput, 0.);
	
	public Expression<Double> levelExpression = new ConstantExpression<Double>(levelOutput, 0.);
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
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
	
	/* none */

}
