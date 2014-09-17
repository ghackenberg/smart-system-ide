package org.xtream.demo.hydro.model.physics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.neuroph.core.NeuralNetwork;

public class Tester
{
	
	public static double[] testRegressionModel(double[] beta, Dataset data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order, int start, int length, String result_path) throws IOException
	{
		System.out.println("testRegressionModel(beta, data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ", " + start + ", " + length + ", \"" + result_path + "\")");
		
		int maximum_past = Math.max(level_past + 1, Math.max(inflow_past, outflow_past));
		
		File result_file = new File(result_path);
		
		result_file.getParentFile().mkdirs();
	
		FileWriter result_writer = new FileWriter(result_file);
		
		result_writer.write("Timepoint;Level measured;Level estimated;Level quotient\n");
		
		int count = 0;
		double error_quadratic = 0;
		double error_max = 0;
		double error_average = 0;
		double[] y_estimated = new double[length + maximum_past];
		
		// Initialize estimated levels
		
		for (int i = 0; i < length + maximum_past; i++)
		{
			y_estimated[i] = data.getLevel(staustufe, start - maximum_past + i);
		}
		
		// Calculate estimated levels
		
		for (int i = maximum_past; i < maximum_past + length; i++)
		{	
			double y_measured = data.getLevel(staustufe, start - maximum_past + i);
			
			// Estimate level
			
			y_estimated[i] = beta[0];

			for (int j = 0; j < level_past; j++)
			{
				for (int k = 0; k < level_order; k++)
				{
					y_estimated[i] += beta[1 + j * level_order + k] * Math.pow(y_estimated[i - 1 - j], k + 1);
				}
			}
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					y_estimated[i] += beta[1 + level_past * level_order + j * inflow_order + k] * Math.pow(data.getInflow(staustufe, start - maximum_past + i - j), k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					y_estimated[i] += beta[1 + level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] * Math.pow(data.getOutflow(staustufe, start - maximum_past + i - j), k + 1);
				}
			}
			
			if (y_estimated[i] == Double.NaN)
			{
				System.out.println("Level estimated not a number: " + i);
				
				break;
			}
			
			result_writer.write(data.getTimepoint(start - maximum_past + i) + ";" + String.valueOf(y_measured).replace('.',',') + ";" + String.valueOf(y_estimated[i]).replace('.',',') + ";" + String.valueOf(y_estimated[i] / y_measured).replace('.',',') + "\n");
			
			count++;

			error_quadratic += (y_measured - y_estimated[i]) * (y_measured - y_estimated[i]);
			error_max = Math.max(error_max, Math.abs(y_measured - y_estimated[i]));
			error_average += Math.abs(y_measured - y_estimated[i]);
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
