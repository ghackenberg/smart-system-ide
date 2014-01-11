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

	private List<String[]> scenario;
	
	public SolarComponent()
	{
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
	
	public Expression<Double> productionExpression = new Expression<Double>(production)
	{
		@Override public Double evaluate(int timepoint)
		{
			try
			{
				return NumberFormat.getInstance().parse(scenario.get(timepoint + 1)[1]).doubleValue() * 1200.;
			}
			catch (ParseException e)
			{
				throw new IllegalStateException(e);
			}
		}
	};
	
	public Expression<Double> consumptionExpression = new ConstantExpression<Double>(consumption, 0.);

}
