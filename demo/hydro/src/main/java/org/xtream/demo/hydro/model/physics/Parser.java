package org.xtream.demo.hydro.model.physics;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Parser
{
	
	public static Dataset parseData(String file) throws IOException
	{
		System.out.println("parseData(\"" + file + "\")");
		
		CSVReader reader = new CSVReader(new FileReader(file), ';');
		
		List<String[]> lines_string = reader.readAll();
		List<String> timepoints = new ArrayList<>();
		List<double[]> measurements = new ArrayList<>();
		
		for (int i = 1; i < lines_string.size(); i++)
		{
			// Check if we have valid level measurements
			
			boolean valid = true;
			
			for (int j = 1 + Constants.LEVEL_INDEX; j < 1 + Constants.STAUSTUFE_COUNT * Constants.STAUSTUFE_MEASUREMENTS + 3; j += Constants.STAUSTUFE_MEASUREMENTS)
			{
				try
				{
					double level = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
					
					valid = valid && level > 0;
				}
				catch (NumberFormatException e)
				{
					valid = false;
				}
			}
			
			// Add the measurements or stop parsing
			
			if (valid)
			{
				timepoints.add(lines_string.get(i)[0]);
				measurements.add(new double[Constants.STAUSTUFE_COUNT * Constants.STAUSTUFE_MEASUREMENTS + 3]);
				
				for (int j = 1; j < 1 + Constants.STAUSTUFE_COUNT * Constants.STAUSTUFE_MEASUREMENTS + 3; j++)
				{
					try
					{
						measurements.get(i - 1)[j - 1] = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
					}
					catch (NumberFormatException e)
					{
						// ignore
					}
				}
			}
			else
			{
				break;
			}
		}
		
		reader.close();
		
		System.out.println(lines_string.size() + " vs " + measurements.size());
		
		/*
		for (int i = 0; i < data_2011.length; i++)
		{
			System.out.print("Row " + i + ": ");
			
			for (int j = 0; j < data_2011[i].length; j++)
			{
				System.out.print((j > 0 ? "," : "") + data_2011[i][j]);
			}
			
			System.out.println();
		}
		*/
		
		return new Dataset(timepoints, measurements);
	}

}
