package org.xtream.demo.hydro.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.Perceptron;

import au.com.bytecode.opencsv.CSVReader;

public class RegressionStudy
{
	
	// Time parameters
	
	public static final int DAY = 4 * 24;
	public static final int WEEK = DAY * 7;
	public static final int MONTH = WEEK * 4;
	
	public static final int WEEK_MIN = 1;
	public static final int WEEK_MAX = 46;
	public static final int WEEK_STEP = 1;
	
	// Level parameters
	
	public static final int LEVEL_PAST_MIN = 1;
	public static final int LEVEL_PAST_MAX = 1;
	public static final int LEVEL_PAST_STEP = 1;
	
	public static final int LEVEL_ORDER_MIN = 1;
	public static final int LEVEL_ORDER_MAX = 1;
	public static final int LEVEL_ORDER_STEP = 1;
	
	// Inflow parameters
	
	public static final int INFLOW_PAST_MIN = 1;
	public static final int INFLOW_PAST_MAX = 10;
	public static final int INFLOW_PAST_STEP = 1;
	
	public static final int INFLOW_ORDER_MIN = 1;
	public static final int INFLOW_ORDER_MAX = 10;
	public static final int INFLOW_ORDER_STEP = 1;
	
	// Outflow parameters
	
	public static final int OUTFLOW_PAST_MIN = 1;
	public static final int OUTFLOW_PAST_MAX = 1;
	public static final int OUTFLOW_PAST_STEP = 1;
	
	public static final int OUTFLOW_ORDER_MIN = 1;
	public static final int OUTFLOW_ORDER_MAX = 1;
	public static final int OUTFLOW_ORDER_STEP = 1;
	
	// Main

	public static void main(String[] args)
	{
		try
		{	
			double[][] data_2011 = parseData("csv/Regression_2011.csv");
			double[][] data_2012 = parseData("csv/Regression_2012.csv");
			
			File configuration_file = new File("csv/Comparison/Configuration.csv");
			
			configuration_file.getParentFile().mkdirs();
			
			FileWriter configuration_writer = new FileWriter(configuration_file);
			
			configuration_writer.write("Staustufe;Level past;Level order;Inflow past;Inflow order;Outflow past;Outflow order;Error average;Error maximum\n");
			
			for (int staustufe = 0; staustufe < 5; staustufe++)
			{
				File overview_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Overview.csv");
				
				overview_file.getParentFile().mkdirs();
				
				FileWriter overview_writer = new FileWriter(overview_file);
				
				overview_writer.write("Configuration;Average;Maximum\n");
				
				double level_past_best = LEVEL_PAST_MIN;
				double level_order_best = LEVEL_ORDER_MIN;
				double inflow_past_best = INFLOW_PAST_MIN;
				double inflow_order_best = INFLOW_ORDER_MIN;
				double outflow_past_best = OUTFLOW_PAST_MIN;
				double outflow_order_best = OUTFLOW_ORDER_MIN;
				
				double error_average_best = Double.MAX_VALUE;
				double error_maximum_best = Double.MAX_VALUE;
				
				for (int level_past = LEVEL_PAST_MIN; level_past <= LEVEL_PAST_MAX; level_past += LEVEL_PAST_STEP)
				{
					for (int inflow_past = INFLOW_PAST_MIN; inflow_past <= INFLOW_PAST_MAX; inflow_past += INFLOW_PAST_STEP)
					{
						for (int outflow_past = OUTFLOW_PAST_MIN; outflow_past <= OUTFLOW_PAST_MAX; outflow_past += OUTFLOW_PAST_STEP)
						{
//							NeuralNetwork<?> network = trainNeuralModel(data_2012, staustufe, level_past, inflow_past, outflow_past);
//
//							for (int i = WEEK_MIN; i <= WEEK_MAX; i += WEEK_STEP)
//							{
//								/*double[] error_neural_2011 = */testNeuralNetwork(network, data_2011, staustufe, level_past, inflow_past, outflow_past, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Neural-" + level_past + "-" + inflow_past + "-" + outflow_past + "/2011/Week_" + i + ".csv");
//								/*double[] error_neural_2012 = */testNeuralNetwork(network, data_2012, staustufe, level_past, inflow_past, outflow_past, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Neural-" + level_past + "-" + inflow_past + "-" + outflow_past + "/2011/Week_" + i + ".csv");
//							}
							
							for (int level_order = LEVEL_ORDER_MIN; level_order <= LEVEL_ORDER_MAX; level_order += LEVEL_ORDER_STEP)
							{
								for (int inflow_order = INFLOW_ORDER_MIN; inflow_order <= INFLOW_ORDER_MAX; inflow_order += INFLOW_ORDER_STEP)
								{
									for (int outflow_order = OUTFLOW_ORDER_MIN; outflow_order <= OUTFLOW_ORDER_MAX; outflow_order += OUTFLOW_ORDER_STEP)
									{	
										overview_writer.write(level_past + "x" + level_order + "," + inflow_past + "x" + inflow_order + "," + outflow_past + "x" + outflow_order + ";");
										
										double[] beta = trainRegressionModel(data_2012, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
										
										File configuration_overview_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Configuration-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + ".csv");
										
										configuration_overview_file.getParentFile().mkdirs();
										
										FileWriter configuration_overview_writer = new FileWriter(configuration_overview_file);
										
										configuration_overview_writer.write("Week;Year 2011 (Average);Year 2011 (Quadratic);Year 2011 (Maximum);Year 2012 (Average);Year 2012 (Quadratic);Year 2012 (Maximum)\n");
										
										double error_average = 0;
										double error_maximum = 0;
										
										int count = 0;
										
										for (int i = WEEK_MIN; i <= WEEK_MAX; i += WEEK_STEP)
										{
											double[] error_regression_2011 = testRegressionModel(beta, data_2011, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Regression-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2011/Week_" + i + ".csv");
											double[] error_regression_2012 = testRegressionModel(beta, data_2012, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Regression-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2012/Week_" + i + ".csv"); 
											
											configuration_overview_writer.write("Week " + (i + 1) + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2011[0]).replace('.',',') + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2011[1]).replace('.',',') + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2011[2]).replace('.',',') + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2012[0]).replace('.',',') + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2012[1]).replace('.',',') + ";");
											configuration_overview_writer.write(String.valueOf(error_regression_2012[2]).replace('.',',') + ";");
											configuration_overview_writer.write("\n");
											
											error_average += error_regression_2011[0] + error_regression_2012[0];
											error_maximum = Math.max(error_maximum, Math.max(error_regression_2011[2], error_regression_2012[2]));
											
											count++;
										}
										
										error_average /= (count * 2);
										
										configuration_overview_writer.close();
										
										if (error_average != Double.NaN && error_maximum != Double.NaN)
										{
											overview_writer.write(String.valueOf(error_average).replace('.',',') + ";");
											overview_writer.write(String.valueOf(error_maximum).replace('.',',') + ";");
											overview_writer.write("\n");
											
											if (error_maximum < error_maximum_best)
											{
												error_maximum_best = error_maximum;
												error_average_best = error_average;
												
												level_past_best = level_past;
												level_order_best = level_order;
												inflow_past_best = inflow_past;
												inflow_order_best = inflow_order;
												outflow_past_best = outflow_past;
												outflow_order_best = outflow_order;
											}
										}
										
										System.out.println("\nbest = " + level_past_best + "x" + level_order_best + "-" + inflow_past_best + "x" + inflow_order_best + "-" + outflow_past_best + "x" + outflow_order_best + "\n");
									}
								}
							}
						}
					}
				}
				
				overview_writer.close();
				
				configuration_writer.write(staustufe + ";");
				configuration_writer.write(level_past_best + ";");
				configuration_writer.write(level_order_best + ";");
				configuration_writer.write(inflow_past_best + ";");
				configuration_writer.write(inflow_order_best + ";");
				configuration_writer.write(outflow_past_best + ";");
				configuration_writer.write(outflow_order_best + "\n");
				configuration_writer.write(error_average_best + "\n");
				configuration_writer.write(error_maximum_best + "\n");
				configuration_writer.flush();
			}
			
			configuration_writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static double[][] parseData(String file) throws IOException
	{
		System.out.println("parseData(\"" + file + "\")");
		
		CSVReader reader = new CSVReader(new FileReader(file), ';');
		
		List<String[]> lines_string = reader.readAll();
		List<double[]> lines_double = new ArrayList<>();
		
		for (int i = 1; i < lines_string.size(); i++)
		{
			// Check if we have valid level measurements
			
			boolean valid = true;
			
			for (int j = 2; j < 16; j+=3)
			{
				double level = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
				
				valid = valid && level > 0;
			}
			
			// Add the measurements or stop parsing
			
			if (valid)
			{
				lines_double.add(new double[15]);
				
				for (int j = 1; j < 16; j++)
				{
					lines_double.get(i - 1)[j - 1] = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
				}
			}
			else
			{
				break;
			}
		}
		
		reader.close();
		
		System.out.println(lines_string.size() + " vs " + lines_double.size());
		
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
		
		return lines_double.toArray(new double[lines_double.size()][15]);
	}
	
	public static double[] trainRegressionModel(double[][] data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		System.out.println("trainRegressionModel(data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ")");
		
		int maximum_past = Math.max(level_past + 1, Math.max(inflow_past, outflow_past));
		
		double[] y = new double[data.length - maximum_past];
		double[][] x = new double[data.length - maximum_past][level_past * level_order + inflow_past * inflow_order + outflow_past * outflow_order];
		
		for (int i = maximum_past; i < data.length; i++)
		{
			y[i - maximum_past] = data[i][staustufe * 3 + 1];
			
			for (int j = 0; j < level_past; j++)
			{
				for (int k = 0; k < level_order; k++)
				{
					x[i - maximum_past][j * level_order + k] = Math.pow(data[i - 1 - j][staustufe * 3 + 1], k + 1);
				}
			}
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + j * inflow_order + k] = Math.pow(data[i - j][staustufe * 3 + 0], k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] = Math.pow(data[i - j][staustufe * 3 + 2], k + 1);
				}
			}
		}
		
		/*
		for (int i = 0; i < data.length - MAXIMUM_PAST; i++)
		{
			System.out.print("Sample " + i + ": " + y[i] + " = ");

			for (int j = 0; j < x[i].length; j++)
			{
				System.out.print((j > 0 ? " + " : "") + "b_" + j + " * " + x[i][j]);
			}
			
			System.out.println();
		}
		*/
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		double[] beta = regression.estimateRegressionParameters();
		
		/*
		for (int i = 0; i < beta.length; i++)
		{
			System.out.println("beta_" + i + " = " + beta[i]);
		}
		
		System.out.println(beta.length);
		System.out.println(LEVEL_PAST * LEVEL_ORDER + INFLOW_PAST * INFLOW_ORDER + OUTFLOW_PAST * OUTFLOW_ORDER);
		*/
		
		/*
		double[] residuals = regression.estimateResiduals();
		double[][] parametersVariance = regression.estimateRegressionParametersVariance();
		double regressandVariance = regression.estimateRegressandVariance();
		double rSquared = regression.calculateRSquared();
		double sigma = regression.estimateRegressionStandardError();
		*/
		
		return beta;
	}
	
	public static double[] testRegressionModel(double[] beta, double[][] data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order, int start, int length, String result_path) throws IOException
	{
		System.out.println("testRegressionModel(beta, data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ", " + start + ", " + length + ", \"" + result_path + "\")");
		
		int maximum_past = Math.max(level_past + 1, Math.max(inflow_past, outflow_past));
		
		File result_file = new File(result_path);
		
		result_file.getParentFile().mkdirs();
	
		FileWriter result_writer = new FileWriter(result_file);
		
		result_writer.write("Measured;Estimated;Quotient\n");
		
		int count = 0;
		double error_quadratic = 0;
		double error_max = 0;
		double error_average = 0;
		double[] y_estimated = new double[length + maximum_past];
		
		// Initialize estimated levels
		
		for (int i = 0; i < length + maximum_past; i++)
		{
			y_estimated[i] = data[start - maximum_past + i][staustufe * 3 + 1];
		}
		
		// Calculate estimated levels
		
		for (int i = maximum_past; i < maximum_past + length; i++)
		{	
			double y_measured = data[start - maximum_past + i][staustufe * 3 + 1];
			
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
					y_estimated[i] += beta[1 + level_past * level_order + j * inflow_order + k] * Math.pow(data[start - maximum_past + i - j][staustufe * 3 + 0], k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					y_estimated[i] += beta[1 + level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] * Math.pow(data[start - maximum_past + i - j][staustufe * 3 + 2], k + 1);
				}
			}
			
			if (y_estimated[i] == Double.NaN)
			{
				System.out.println("Level estimated not a number: " + i);
				
				break;
			}
			
			result_writer.write(String.valueOf(y_measured).replace('.',',') + ";" + String.valueOf(y_estimated[i]).replace('.',',') + ";" + String.valueOf(y_estimated[i] / y_measured).replace('.',',') + "\n");
			
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
	
	public static NeuralNetwork<?> trainNeuralModel(double[][] data, int staustufe, int level_past, int inflow_past, int outflow_past)
	{
		System.out.println("trainNeuralModel(data, " + staustufe + ", " + level_past + ", " + inflow_past + ", " + outflow_past + ")");
		
		int maximum_past = Math.max(level_past, Math.max(inflow_past, outflow_past));
		
		DataSet training = new DataSet(level_past + inflow_past + outflow_past, 1);
		
		for (int i = maximum_past; i < data.length / 1000; i++)
		{
			double[] input = new double[level_past + inflow_past + outflow_past];
			double[] output = new double[1];
			
			for (int j = 0; j < level_past;  j++)
			{
				input[j] = data[i - 1 - j][1];
			}
			for (int j = 0; j < inflow_past;  j++)
			{
				input[level_past + j] = data[i - j][0];
			}
			for (int j = 0; j < outflow_past;  j++)
			{
				input[level_past + inflow_past + j] = data[i - j][2];
			}
			output[0] = data[i][1];
			
			training.addRow(input, output);
		}
		
		NeuralNetwork<?> network = new Perceptron(level_past + inflow_past + outflow_past, 1);
		network.learn(training);
		
		return network;
	}
	
	public static double[] testNeuralNetwork(NeuralNetwork<?> network, double[][] data, int staustufe, int level_past, int inflow_past, int outflow_past, int start, int length, String result_path) throws IOException
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
