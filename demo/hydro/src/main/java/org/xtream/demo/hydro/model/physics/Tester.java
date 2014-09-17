package org.xtream.demo.hydro.model.physics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.neuroph.core.NeuralNetwork;

public class Tester
{
	
	public static double[] testRegressionModel(Polynom model, Dataset data, int staustufe, int start, int length, String result_path) throws IOException
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
			level_past[i] = data.getLevel(staustufe, start - 1 - model.getLevelPast() + i);
		}
		
		// Calculate estimated levels
		
		for (int i = start; i < start + length; i++)
		{
			for (int j = 0; j < model.getInflowPast(); j++)
			{
				inflow_past[j] = data.getInflow(staustufe, i - j);
			}
			for (int j = 0; j < model.getOutflowPast(); j++)
			{
				outflow_past[j] = data.getOutflowTotal(staustufe, i - j);
			}
			
			double level_measured = data.getLevel(staustufe, start - model.getMaximumPast() + i);
			double level_estimated = model.estimate(level_past, inflow_past, outflow_past);
			
			if (level_estimated == Double.NaN)
			{
				System.out.println("Level estimated not a number: " + i);
				
				break;
			}
			
			result_writer.write(data.getTimepoint(start - model.getMaximumPast() + i) + ";" + String.valueOf(level_measured).replace('.',',') + ";" + String.valueOf(level_estimated).replace('.',',') + ";" + String.valueOf(level_estimated / level_measured).replace('.',',') + "\n");
			
			count++;

			error_quadratic += (level_measured - level_estimated) * (level_measured - level_estimated);
			error_max = Math.max(error_max, Math.abs(level_measured - level_estimated));
			error_average += Math.abs(level_measured - level_estimated);
			
			for (int j = model.getLevelPast() - 1; j > 0; j--)
			{
				level_past[j] = level_past[j - 1];
			}
			level_past[0] = level_estimated;
		}
		
		error_quadratic /= count;
		error_average /= count;
		
		//System.out.println("Quadratic error: " + error);
		
		result_writer.close();
		
		return new double[] {error_average, error_quadratic, error_max};
	}
	
	public static double[] testNeuralNetwork(NeuralNetwork<?> network, Dataset data, int staustufe, int level_past, int inflow_past, int outflow_past, int start, int length, String result_path) throws IOException
	{
		System.out.println("testNeuralModel(network, data, " + staustufe + ", " + level_past + ", " + inflow_past + ", " + outflow_past + ", " + start + ", " + length + ", \"" + result_path + "\")");
		
		File result_file = new File(result_path);
		
		result_file.getParentFile().mkdirs();
	
		FileWriter result_writer = new FileWriter(result_file);
		
		// TODO implement test procedure!
		
		result_writer.close();
		
		return null;
	}

}
