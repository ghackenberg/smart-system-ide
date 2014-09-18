package org.xtream.demo.hydro.model.physics;

import java.io.File;
import java.io.FileWriter;

public class PolynomOptimizer
{
	
	// Time parameters
	
	public static final int DAY = 4 * 24;
	public static final int WEEK = DAY * 7;
	public static final int MONTH = WEEK * 4;
	
	public static final int WEEK_MIN = 1;
	public static final int WEEK_MAX = 40;
	public static final int WEEK_STEP = 1;
	
	// Level parameters
	
	public static final int LEVEL_PAST_MIN = 1;
	public static final int LEVEL_PAST_MAX = 1;
	public static final int LEVEL_PAST_STEP = 1;
	
	public static final int LEVEL_ORDER_MIN = 1;
	public static final int LEVEL_ORDER_MAX = 1;
	public static final int LEVEL_ORDER_STEP = 1;
	
	// Inflow parameters
	
	public static final int INFLOW_PAST_MIN = 5;
	public static final int INFLOW_PAST_MAX = 5;
	public static final int INFLOW_PAST_STEP = 1;
	
	public static final int INFLOW_ORDER_MIN = 1;
	public static final int INFLOW_ORDER_MAX = 10;
	public static final int INFLOW_ORDER_STEP = 2;
	
	// Outflow parameters
	
	public static final int OUTFLOW_PAST_MIN = 1;
	public static final int OUTFLOW_PAST_MAX = 1;
	public static final int OUTFLOW_PAST_STEP = 1;
	
	public static final int OUTFLOW_ORDER_MIN = 1;
	public static final int OUTFLOW_ORDER_MAX = 1;
	public static final int OUTFLOW_ORDER_STEP = 1;
	
	// Turbine outflow past parameters
	
	public static final int TURBINE_OUTFLOW_PAST_MIN = 1;
	public static final int TURBINE_OUTFLOW_PAST_MAX = 2;
	public static final int TURBINE_OUTFLOW_PAST_STEP = 1;
	
	// Turbine outflow order parameters
	
	public static final int TURBINE_OUTFLOW_ORDER_MIN = 1;
	public static final int TURBINE_OUTFLOW_ORDER_MAX = 5;
	public static final int TURBINE_OUTFLOW_ORDER_STEP = 2;
	
	// Upper level past parameters
	
	public static final int UPPER_LEVEL_PAST_MIN = 1;
	public static final int UPPER_LEVEL_PAST_MAX = 2;
	public static final int UPPER_LEVEL_PAST_STEP = 1;
	
	// Upper level order parameters
	
	public static final int UPPER_LEVEL_ORDER_MIN = 1;
	public static final int UPPER_LEVEL_ORDER_MAX = 5;
	public static final int UPPER_LEVEL_ORDER_STEP = 2;
	
	// Lower level past parameters
	
	public static final int LOWER_LEVEL_PAST_MIN = 1;
	public static final int LOWER_LEVEL_PAST_MAX = 2;
	public static final int LOWER_LEVEL_PAST_STEP = 1;
	
	// Lower level order parameters
	
	public static final int LOWER_LEVEL_ORDER_MIN = 1;
	public static final int LOWER_LEVEL_ORDER_MAX = 5;
	public static final int LOWER_LEVEL_ORDER_STEP = 2;
	
	// Main

	public static void main(String[] args)
	{
		try
		{
			// Datasets
			
			Dataset data_2011 = DatasetParser.parseData("csv/Regression_2011.csv");
			Dataset data_2012 = DatasetParser.parseData("csv/Regression_2012.csv");
			
			// Files
			
			File level_file = new File("csv/Comparison/Level.csv");
			level_file.getParentFile().mkdirs();
			
			File production_file = new File("csv/Comparison/Production.csv");
			production_file.getParentFile().mkdirs();
			
			// Writers
			
			FileWriter level_writer = new FileWriter(level_file);
			level_writer.write("Staustufe;Level past;Level order;Inflow past;Inflow order;Outflow past;Outflow order;Error average;Error maximum\n");
			
			FileWriter production_writer = new FileWriter(production_file);
			production_writer.write("Staustufe;Turbine Outflow Past;Turbine Outflow Order;Upper Level Past;Upper Level Order;Lower Level Past;Lower Level Order;Error average;Error maximum\n");
			
			// Seach procedure
			
			for (int staustufe = 0; staustufe < 6; staustufe++)
			{
				// Files
				
				File staustufe_level_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Level.csv");
				staustufe_level_file.getParentFile().mkdirs();
				
				// Writers
				
				FileWriter staustufe_level_writer = new FileWriter(staustufe_level_file);
				staustufe_level_writer.write("Configuration;Error average;Error maximum\n");
				
				// Parameters
				
				double level_past_best = LEVEL_PAST_MIN;
				double level_order_best = LEVEL_ORDER_MIN;
				double inflow_past_best = INFLOW_PAST_MIN;
				double inflow_order_best = INFLOW_ORDER_MIN;
				double outflow_past_best = OUTFLOW_PAST_MIN;
				double outflow_order_best = OUTFLOW_ORDER_MIN;
				
				double level_error_average_best = Double.MAX_VALUE;
				double level_error_maximum_best = Double.MAX_VALUE;
				
				// Search procedure
				
				for (int level_past = LEVEL_PAST_MIN; level_past <= LEVEL_PAST_MAX; level_past += LEVEL_PAST_STEP)
				{
					for (int inflow_past = INFLOW_PAST_MIN; inflow_past <= INFLOW_PAST_MAX; inflow_past += INFLOW_PAST_STEP)
					{
						for (int outflow_past = OUTFLOW_PAST_MIN; outflow_past <= OUTFLOW_PAST_MAX; outflow_past += OUTFLOW_PAST_STEP)
						{
							for (int level_order = LEVEL_ORDER_MIN; level_order <= LEVEL_ORDER_MAX; level_order += LEVEL_ORDER_STEP)
							{
								for (int inflow_order = INFLOW_ORDER_MIN; inflow_order <= INFLOW_ORDER_MAX; inflow_order += INFLOW_ORDER_STEP)
								{
									for (int outflow_order = OUTFLOW_ORDER_MIN; outflow_order <= OUTFLOW_ORDER_MAX; outflow_order += OUTFLOW_ORDER_STEP)
									{	
										// Files

										File config_staustufe_level_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Level/Configuration-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + ".csv");
										config_staustufe_level_file.getParentFile().mkdirs();
										
										// Writers

										FileWriter config_staustufe_level_writer = new FileWriter(config_staustufe_level_file);
										config_staustufe_level_writer.write("Week;Year 2011 (Average);Year 2011 (Quadratic);Year 2011 (Maximum);Year 2012 (Average);Year 2012 (Quadratic);Year 2012 (Maximum)\n");
										
										// Model
										
										PolynomLevel model = new PolynomLevel(staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
										model.fit(data_2012);
										
										// Error
										
										double error_average = 0;
										double error_maximum = 0;
										
										int count = 0;
										
										for (int i = WEEK_MIN; i <= WEEK_MAX; i += WEEK_STEP)
										{
											double[] error_regression_2011 = PolynomTester.testLevelModel(model, data_2011, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Level/Data-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2011/Week_" + i + ".csv");
											double[] error_regression_2012 = PolynomTester.testLevelModel(model, data_2012, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Level/Data-" + level_past + "x" + level_order + "-" + inflow_past + "x" + inflow_order + "-" + outflow_past + "x" + outflow_order + "/2012/Week_" + i + ".csv"); 
											
											config_staustufe_level_writer.write("Week " + (i + 1) + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2011[0]).replace('.',',') + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2011[1]).replace('.',',') + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2011[2]).replace('.',',') + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2012[0]).replace('.',',') + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2012[1]).replace('.',',') + ";");
											config_staustufe_level_writer.write(String.valueOf(error_regression_2012[2]).replace('.',',') + ";");
											config_staustufe_level_writer.write("\n");
											
											error_average += error_regression_2011[0] + error_regression_2012[0];
											error_maximum = Math.max(error_maximum, Math.max(error_regression_2011[2], error_regression_2012[2]));
											
											count++;
										}
										
										error_average /= (count * 2);
										
										config_staustufe_level_writer.close();
										
										// Remember better
										
										if (error_average != Double.NaN && error_maximum != Double.NaN)
										{
											staustufe_level_writer.write(level_past + "x" + level_order + "," + inflow_past + "x" + inflow_order + "," + outflow_past + "x" + outflow_order + ";");
											staustufe_level_writer.write(String.valueOf(error_average).replace('.',',') + ";");
											staustufe_level_writer.write(String.valueOf(error_maximum).replace('.',',') + ";");
											staustufe_level_writer.write("\n");
											
											if (error_maximum < level_error_maximum_best)
											{
												level_error_maximum_best = error_maximum;
												level_error_average_best = error_average;
												
												level_past_best = level_past;
												level_order_best = level_order;
												inflow_past_best = inflow_past;
												inflow_order_best = inflow_order;
												outflow_past_best = outflow_past;
												outflow_order_best = outflow_order;
											}
										}
										
										// Debug
										
										System.out.println("\nbest = " + level_past_best + "x" + level_order_best + "-" + inflow_past_best + "x" + inflow_order_best + "-" + outflow_past_best + "x" + outflow_order_best + "\n");
									}
								}
							}
						}
					}
				}
				
				staustufe_level_writer.close();
				
				// Remember best parameters
				
				level_writer.write(staustufe + ";");
				level_writer.write(level_past_best + ";");
				level_writer.write(level_order_best + ";");
				level_writer.write(inflow_past_best + ";");
				level_writer.write(inflow_order_best + ";");
				level_writer.write(outflow_past_best + ";");
				level_writer.write(outflow_order_best + ";");
				level_writer.write(level_error_average_best + ";");
				level_writer.write(level_error_maximum_best + "\n");
				level_writer.flush();

				if (staustufe < 1)
				{
					// Files
					
					File staustufe_production_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Production.csv");
					staustufe_production_file.getParentFile().mkdirs();
					
					// Writers
					
					FileWriter staustufe_production_writer = new FileWriter(staustufe_production_file);
					staustufe_production_writer.write("Configuration;Error average;Error maximum\n");
					
					// Parameters
					
					double turbine_outflow_past_best = TURBINE_OUTFLOW_PAST_MIN;
					double turbine_outflow_order_best = TURBINE_OUTFLOW_ORDER_MIN;
					double upper_level_past_best = UPPER_LEVEL_PAST_MIN;
					double upper_level_order_best = UPPER_LEVEL_ORDER_MIN;
					double lower_level_past_best = LOWER_LEVEL_PAST_MIN;
					double lower_level_order_best = LOWER_LEVEL_ORDER_MIN;
					
					double production_error_average_best = Double.MAX_VALUE;
					double production_error_maximum_best = Double.MAX_VALUE;
					
					// Search procedure
					
					for (int turbine_outflow_past = TURBINE_OUTFLOW_PAST_MIN; turbine_outflow_past <= TURBINE_OUTFLOW_PAST_MAX; turbine_outflow_past += TURBINE_OUTFLOW_PAST_STEP)
					{
						for (int upper_level_past = UPPER_LEVEL_PAST_MIN; upper_level_past <= UPPER_LEVEL_PAST_MAX; upper_level_past += UPPER_LEVEL_PAST_STEP)
						{
							for (int lower_level_past = LOWER_LEVEL_PAST_MIN; lower_level_past <= LOWER_LEVEL_PAST_MAX; lower_level_past += LOWER_LEVEL_PAST_STEP)
							{
								for (int turbine_outflow_order = TURBINE_OUTFLOW_ORDER_MIN; turbine_outflow_order <= TURBINE_OUTFLOW_ORDER_MAX; turbine_outflow_order += TURBINE_OUTFLOW_ORDER_STEP)
								{
									for (int upper_level_order = UPPER_LEVEL_ORDER_MIN; upper_level_order <= UPPER_LEVEL_ORDER_MAX; upper_level_order += UPPER_LEVEL_ORDER_STEP)
									{
										for (int lower_level_order = LOWER_LEVEL_ORDER_MIN; lower_level_order <= LOWER_LEVEL_ORDER_MAX; lower_level_order += LOWER_LEVEL_ORDER_STEP)
										{
											// Files

											File config_staustufe_production_file = new File("csv/Comparison/Staustufe-" + staustufe + "/Production/Configuration-" + turbine_outflow_past + "x" + turbine_outflow_order + "-" + upper_level_past + "x" + upper_level_order + "-" + lower_level_past + "x" + lower_level_order + ".csv");
											config_staustufe_production_file.getParentFile().mkdirs();
											
											// Writers

											FileWriter config_staustufe_production_writer = new FileWriter(config_staustufe_production_file);
											config_staustufe_production_writer.write("Week;Year 2011 (Average);Year 2011 (Quadratic);Year 2011 (Maximum);Year 2012 (Average);Year 2012 (Quadratic);Year 2012 (Maximum)\n");
											
											// Model
											
											PolynomProduction model = new PolynomProduction(staustufe, turbine_outflow_past, turbine_outflow_order, upper_level_past, upper_level_order, lower_level_past, lower_level_order);
											model.fit(data_2012);
											
											// Error
											
											double error_average = 0;
											double error_maximum = 0;
											
											int count = 0;
											
											for (int i = WEEK_MIN; i <= WEEK_MAX; i += WEEK_STEP)
											{
												double[] error_2011 = PolynomTester.testProductionModel(model, data_2011, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Production/Data-" + turbine_outflow_past + "x" + turbine_outflow_order + "-" + upper_level_past + "x" + upper_level_order + "-" + lower_level_past + "x" + lower_level_order + "/2011/Week_" + i + ".csv");
												double[] error_2012 = PolynomTester.testProductionModel(model, data_2012, WEEK * i, WEEK * 1, "csv/Comparison/Staustufe-" + staustufe + "/Production/Data-" + turbine_outflow_past + "x" + turbine_outflow_order + "-" + upper_level_past + "x" + upper_level_order + "-" + lower_level_past + "x" + lower_level_order + "/2012/Week_" + i + ".csv");

												config_staustufe_production_writer.write("Week " + (i + 1) + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2011[0]).replace('.',',') + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2011[1]).replace('.',',') + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2011[2]).replace('.',',') + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2012[0]).replace('.',',') + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2012[1]).replace('.',',') + ";");
												config_staustufe_production_writer.write(String.valueOf(error_2012[2]).replace('.',',') + ";");
												config_staustufe_production_writer.write("\n");
												
												error_average += error_2011[0] + error_2012[0];
												error_maximum = Math.max(error_maximum, Math.max(error_2011[2], error_2012[2]));
												
												count++;
											}
											
											error_average /= (count * 2);
											
											config_staustufe_production_writer.close();
											
											// Remember better
											
											if (error_average != Double.NaN && error_maximum != Double.NaN)
											{
												staustufe_production_writer.write(turbine_outflow_past + "x" + turbine_outflow_order + "-" + upper_level_past + "x" + upper_level_order + "-" + lower_level_past + "x" + lower_level_order + ";");
												staustufe_production_writer.write(String.valueOf(error_average).replace('.',',') + ";");
												staustufe_production_writer.write(String.valueOf(error_maximum).replace('.',',') + ";");
												staustufe_production_writer.write("\n");
												
												if (error_maximum < production_error_maximum_best)
												{
													production_error_maximum_best = error_maximum;
													production_error_average_best = error_average;
													
													turbine_outflow_past_best = turbine_outflow_past;
													turbine_outflow_order_best = turbine_outflow_order;
													upper_level_past_best = upper_level_past;
													upper_level_order_best = upper_level_order;
													lower_level_past_best = lower_level_past;
													lower_level_order_best = lower_level_order;
												}
											}
											
											// Debug
											
											System.out.println("\nbest = " + turbine_outflow_past_best + "x" + turbine_outflow_order_best + "-" + upper_level_past_best + "x" + upper_level_order_best + "-" + lower_level_past_best + "x" + lower_level_order_best + "\n");
										}
									}
								}	
							}
						}
					}
					
					staustufe_production_writer.close();
					
					// Remember best parameters
					
					production_writer.write(staustufe + ";");
					production_writer.write(turbine_outflow_past_best + ";");
					production_writer.write(turbine_outflow_order_best + ";");
					production_writer.write(upper_level_past_best + ";");
					production_writer.write(upper_level_order_best + ";");
					production_writer.write(lower_level_past_best + ";");
					production_writer.write(lower_level_order_best + ";");
					production_writer.write(production_error_average_best + ";");
					production_writer.write(production_error_maximum_best + "\n");
					production_writer.flush();
				}
			}
			
			// Finish up
			
			level_writer.close();
			
			production_writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
