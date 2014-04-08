package org.xtream.demo.hydro.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

import au.com.bytecode.opencsv.CSVReader;

public class ScenarioComponent extends Component
{
	
	// Constants

	public static String INFLOW_1 = "csv/Inflow_week_2_2011.csv";
	public static String INFLOW_2 = "csv/Inflow_week_6_2011.csv";
	public static String INFLOW_3 = "csv/Inflow_week_14_2011.csv";
	public static String INFLOW_4 = "csv/Inflow_week_24_2011.csv";
	
	public static String PRICE_1 = "csv/Price_week_2_2011.csv";
	public static String PRICE_2 = "csv/Price_week_6_2011.csv";
	public static String PRICE_3 = "csv/Price_week_14_2011.csv";
	public static String PRICE_4 = "csv/Price_week_24_2011.csv";
	
	// Constructors
	
	public ScenarioComponent(String inflowFile, String priceFile)
	{
		try
		{
			// Read inflows
			
			CSVReader inflowReader = new CSVReader(new FileReader(inflowFile), ';');
			
			for (String[] line : inflowReader.readAll())
			{
				inflows.add(Double.parseDouble(line[0].replace(',','.')));
			}
			
			inflowReader.close();
			
			// Read prices
			
			CSVReader priceReader = new CSVReader(new FileReader(priceFile), ';');
			
			for (String[] line : priceReader.readAll())
			{
				prices.add(Double.parseDouble(line[0].replace(',','.')));
			}
			
			priceReader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// Parameters
	
	protected Vector<Double> inflows = new Vector<>();
	protected Vector<Double> prices = new Vector<>();
	
	// Ports
	
	public Port<Double> inflowOutput = new Port<>();
	public Port<Double> priceOutput = new Port<>();
	
	// Charts
	
	public Chart inflowChart = new Chart(inflowOutput);
	public Chart priceChart = new Chart(priceOutput);
	
	// Expressions
	
	public Expression<Double> inflowExpression = new Expression<Double>(inflowOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return inflows.get(timepoint);
		}
	};
	public Expression<Double> priceExpression = new Expression<Double>(priceOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return prices.get(timepoint);
		}
	};

}
