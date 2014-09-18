package org.xtream.demo.hydro.model.physics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tester
{
	
	public static double[] testLevelModel(PolynomLevel model, Dataset data, int staustufe, int start, int length, String result_path) throws IOException
	{
		System.out.println("testRegressionModel(beta, data, " + staustufe + ", " + start + ", " + length + ", \"" + result_path + "\")");
		
		File result_file = new File(result_path);
		
		result_file.getParentFile().mkdirs();
	
		FileWriter result_writer = new FileWriter(result_file);
		
		result_writer.write("Timepoint;Level measured;Level estimated;Level quotient\n");
		
		int count = 0;
		
		double error_quadratic = 0;
		double error_max = 0;
		double error_average = 0;
		
		double[] level_past = new double[model.getLevelPast()];
		double[] inflow_past = new double[model.getInflowPast()];
		double[] outflow_past = new double[model.getOutflowPast()];
		
		// Initialize level past
		
		for (int i = 0; i < model.getLevelPast(); i++)
		{
			level_past[i] = data.getLevel(staustufe, start - model.getLevelPast() + i);
		}
		
		// Calculate estimated levels
		
		for (int i = start; i < start + length; i++)
		{
			for (int j = 0; j < model.getInflowPast(); j++)
			{
				inflow_past[j] = data.getInflow(staustufe, i - model.getInflowPast() + j + 1);
			}
			for (int j = 0; j < model.getOutflowPast(); j++)
			{
				outflow_past[j] = data.getOutflowTotal(staustufe, i - model.getOutflowPast() + j + 1);
			}
			
			double level_measured = data.getLevel(staustufe, i);
			double level_estimated = model.estimate(level_past, inflow_past, outflow_past);
			
			if (level_estimated == Double.NaN)
			{
				result_writer.close();
				
				throw new IllegalStateException("Level estimated is not a number!");
			}
			
			result_writer.write(data.getTimepoint(i) + ";" + String.valueOf(level_measured).replace('.',',') + ";" + String.valueOf(level_estimated).replace('.',',') + ";" + String.valueOf(level_estimated / level_measured).replace('.',',') + "\n");
			
			count++;

			error_quadratic += (level_measured - level_estimated) * (level_measured - level_estimated);
			error_max = Math.max(error_max, Math.abs(level_measured - level_estimated));
			error_average += Math.abs(level_measured - level_estimated);
			
			for (int j = 1; j < model.getLevelPast(); j++)
			{
				level_past[j - 1] = level_past[j];
			}
			level_past[model.getLevelPast() - 1] = level_estimated;
		}
		
		error_quadratic /= count;
		error_average /= count;
		
		//System.out.println("Quadratic error: " + error);
		
		result_writer.close();
		
		return new double[] {error_average, error_quadratic, error_max};
	}
	
	public static double[] testProductionModel()
	{
		throw new IllegalStateException("Not implemented yet!");
	}

}
