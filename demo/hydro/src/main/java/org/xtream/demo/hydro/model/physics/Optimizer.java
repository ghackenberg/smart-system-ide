package org.xtream.demo.hydro.model.physics;

import java.io.File;
import java.io.FileWriter;

public class Optimizer
{
	
	// Time parameters
	
	public static final int DAY = 4 * 24;
	public static final int WEEK = DAY * 7;
	public static final int MONTH = WEEK * 4;
	
	public static final int WEEK_MIN = 1;
	public static final int WEEK_MAX = 6;
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
			Dataset data_2011 = Parser.parseData("csv/Regression_2011.csv");
			Dataset data_2012 = Parser.parseData("csv/Regression_2012.csv");
			
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
										
										double[] beta = Trainer.trainRegressionModel(data_2012, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
										
										File configuration_overview_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Configuration-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + ".csv");
										
										configuration_overview_file.getParentFile().mkdirs();
										
										FileWriter configuration_overview_writer = new FileWriter(configuration_overview_file);
										
										configuration_overview_writer.write("Week;Year 2011 (Average);Year 2011 (Quadratic);Year 2011 (Maximum);Year 2012 (Average);Year 2012 (Quadratic);Year 2012 (Maximum)\n");
										
										double error_average = 0;
										double error_maximum = 0;
										
										int count = 0;
										
										for (int i = WEEK_MIN; i <= WEEK_MAX; i += WEEK_STEP)
										{
											double[] error_regression_2011 = Tester.testRegressionModel(beta, data_2011, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Regression-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2011/Week_" + i + ".csv");
											double[] error_regression_2012 = Tester.testRegressionModel(beta, data_2012, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Regression-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2012/Week_" + i + ".csv"); 
											
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

}
