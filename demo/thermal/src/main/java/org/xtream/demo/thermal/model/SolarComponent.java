package org.xtream.demo.thermal.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Show;

import au.com.bytecode.opencsv.CSVReader;

public class SolarComponent extends Component
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

	@Show("Main")
	public OutputPort<Double> energy = new OutputPort<>();
	
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
	
	public Expression<Double> energyExpression = new Expression<Double>(energy)
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

}
