package org.xtream.core.optimizer.beam;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.NumberFormat;

import org.xtream.core.model.Component;
import org.xtream.core.utilities.monitors.CalibrationMonitor;

public abstract class Calibrator<T extends Component>
{
	
	private T root;
	
	public Calibrator(T root)
	{
		this.root = root;
	}
	
	public void run(int processors, int duration, int classes_start, int classes_end, int classes_steps, int samples_start, int samples_end, int samples_steps, double random_start, double random_end, int random_steps, int iterations)
	{
		try
		{
			double[][][] fails = new double[classes_steps][samples_steps][random_steps];
			double[][][] time_averages = new double[classes_steps][samples_steps][random_steps];
			double[][][] time_covariances = new double[classes_steps][samples_steps][random_steps];
			double[][][] cost_averages = new double[classes_steps][samples_steps][random_steps];
			double[][][] cost_covariances = new double[classes_steps][samples_steps][random_steps];
			
			double time_averages_max = Double.MIN_VALUE;
			double time_averages_min = Double.MAX_VALUE;
			
			double time_covariances_max = Double.MIN_VALUE;
			double time_covariances_min = Double.MAX_VALUE;
			
			double cost_averages_max = Double.MIN_VALUE;
			double cost_averages_min = Double.MAX_VALUE;
			
			double cost_covariances_max = Double.MIN_VALUE;
			double cost_covariances_min = Double.MAX_VALUE;
			
			for (int classes_i = 0; classes_i < classes_steps; classes_i++)
			{
				int classes = getClasses(classes_start, classes_end, classes_steps, classes_i);
				
				for (int samples_i = 0; samples_i < samples_steps; samples_i++)
				{
					int samples = getSamples(samples_start, samples_end, samples_steps, samples_i);
							
					for (int random_i = 0; random_i < random_steps; random_i++)
					{
						double random = getRandom(random_start, random_end, random_steps, random_i);
						
						System.out.println("Generating sample " + classes + "/" + samples + "/" + random);
						
						double[] times = new double[iterations];
						double[] costs = new double[iterations]; 
						
						for (int iteration = 0; iteration < iterations; iteration++)
						{
							CalibrationMonitor<T> monitor = new CalibrationMonitor<>();
							
							new Engine<>(root, samples, classes, 1, random, processors).run(duration, true, monitor);
							
							System.out.print(iteration + " ");					
							
							if (monitor.timepoint < duration - 1)
							{
								fails[classes_i][samples_i][random_i]++;
								
								times[iteration] = Double.MAX_VALUE;
								costs[iteration] = Double.MAX_VALUE;
							}
							else
							{
								times[iteration] = monitor.used_time;
								costs[iteration] = monitor.min_objective;
							}
							
						}
						
						System.out.println();
						
						double time_average = 0.;
						double cost_average = 0.;
						
						for (int iteration = 0; iteration < iterations; iteration++)
						{
							if (times[iteration] != Double.MAX_VALUE)
							{
								time_average += times[iteration] / (iterations - fails[classes_i][samples_i][random_i]);
							}
							if (costs[iteration] != Double.MAX_VALUE)
							{
								cost_average += costs[iteration] / (iterations - fails[classes_i][samples_i][random_i]);
							}
						}
						
						double time_covariance = 0.;
						double cost_covariance = 0.;
						
						for (int iteration = 0; iteration < iterations; iteration++)
						{
							if (times[iteration] != Double.MAX_VALUE)
							{
								double difference = times[iteration] - time_average;
								
								time_covariance += difference * difference / (iterations - fails[classes_i][samples_i][random_i]);
							}
							if (costs[iteration] != Double.MAX_VALUE)
							{
								double difference = costs[iteration] - cost_average;
								cost_covariance += difference * difference / (iterations - fails[classes_i][samples_i][random_i]);
							}
						}
						
						time_averages[classes_i][samples_i][random_i] = time_average;
						time_covariances[classes_i][samples_i][random_i] = time_covariance;
						cost_averages[classes_i][samples_i][random_i] = cost_average;
						cost_covariances[classes_i][samples_i][random_i] = cost_covariance;
						
						time_averages_max = Math.max(time_averages_max, time_average);
						time_averages_min = Math.min(time_averages_min, time_average);
						
						time_covariances_max = Math.max(time_covariances_max, time_covariance);
						time_covariances_min = Math.min(time_covariances_min, time_covariance);
						
						cost_averages_max = Math.max(cost_averages_max, cost_average);
						cost_averages_min = Math.min(cost_averages_min, cost_average);
						
						cost_covariances_max = Math.max(cost_covariances_max, cost_covariance);
						cost_covariances_min = Math.min(cost_covariances_min, cost_covariance);
					}
				}
			}
			
			for (int random_i = 0; random_i < random_steps; random_i++)
			{
				PrintStream cost_averages_out = new PrintStream("Cost-" + random_i + "-Averages.csv");
				PrintStream cost_covariances_out = new PrintStream("Cost-" + random_i + "-Covariances.csv");
				PrintStream time_averages_out = new PrintStream("Time-" + random_i + "-Averages.csv");
				PrintStream time_covariances_out = new PrintStream("Time-" + random_i + "-Covariances.csv");
				
				for (int samples_i = 0; samples_i < samples_steps; samples_i++)
				{
					int samples = getSamples(samples_start, samples_end, samples_steps, samples_i);
					
					cost_averages_out.print(";" + samples);
					cost_covariances_out.print(";" + samples);
					time_averages_out.print(";" + samples);
					time_covariances_out.print(";" + samples);
				}
				cost_averages_out.println();
				cost_covariances_out.println();
				time_averages_out.println();
				time_covariances_out.println();
				
				for (int classes_i = 0; classes_i < classes_steps; classes_i++)
				{
					int classes = getClasses(classes_start, classes_end, classes_steps, classes_i);
					
					cost_averages_out.print(classes);
					cost_covariances_out.print(classes);
					time_averages_out.print(classes);
					time_covariances_out.print(classes);
					
					for (int samples_i = 0; samples_i < samples_steps; samples_i++)
					{
						cost_averages_out.print(";" + NumberFormat.getInstance().format(cost_averages[classes_i][samples_i][random_i]));
						cost_covariances_out.print(";" + NumberFormat.getInstance().format(cost_covariances[classes_i][samples_i][random_i]));
						time_averages_out.print(";" + NumberFormat.getInstance().format(time_averages[classes_i][samples_i][random_i]));
						time_covariances_out.print(";" + NumberFormat.getInstance().format(time_covariances[classes_i][samples_i][random_i]));
					}
					cost_averages_out.println();
					cost_covariances_out.println();
					time_averages_out.println();
					time_covariances_out.println();
				}
				
				cost_averages_out.close();
				cost_covariances_out.close();
				time_averages_out.close();
				time_covariances_out.close();
			}
			
			if (random_steps > 0)
			{
				PrintStream time_out = new PrintStream("Time.pov");
				PrintStream cost_out = new PrintStream("Cost.pov");
				PrintStream cost_minima_out = new PrintStream("CostMinima.pov");
				PrintStream cost_classes_out = new PrintStream("CostClasses.pov");
				PrintStream cost_samples_out = new PrintStream("CostSamples.pov");
				PrintStream cost_random_out = new PrintStream("CostRandom.pov");
				
				printHeader(time_out, classes_steps, samples_steps, random_steps);
				printHeader(cost_out, classes_steps, samples_steps, random_steps);
				printHeader(cost_minima_out, classes_steps, samples_steps, random_steps);
				printHeader(cost_classes_out, classes_steps, samples_steps, random_steps);
				printHeader(cost_samples_out, classes_steps, samples_steps, random_steps);
				printHeader(cost_random_out, classes_steps, samples_steps, random_steps);
				
				printYZGrid(cost_classes_out, 0, random_steps, samples_steps, 1);
				printXZGrid(cost_random_out, classes_steps, 0, samples_steps, 1);
				printXYGrid(cost_samples_out, classes_steps, random_steps, 0, 1);
				
				time_out.println("// body");
				cost_out.println("// body");
				cost_minima_out.println("// body");
				cost_classes_out.println("// body");
				cost_samples_out.println("// body");
				cost_random_out.println("// body");
				
				for (int classes_i = 0; classes_i < classes_steps; classes_i++)
				{
					for (int samples_i = 0; samples_i < samples_steps; samples_i++)
					{
						for (int random_i = 0; random_i < random_steps; random_i++)
						{
							double time_average = time_averages[classes_i][samples_i][random_i];
							double time_covariance = time_covariances[classes_i][samples_i][random_i];
							double cost_average = cost_averages[classes_i][samples_i][random_i];
							double cost_covariance = cost_covariances[classes_i][samples_i][random_i];
							
							double time_average_norm = (time_average - time_averages_min) / (time_averages_max - time_averages_min);
							double time_covariance_norm = (time_covariance - time_covariances_min) / (time_covariances_max - time_covariances_min);
							double cost_average_norm = (cost_average - cost_averages_min) / (cost_averages_max - cost_averages_min);
							double cost_covariance_norm = (cost_covariance - cost_covariances_min) / (cost_covariances_max - cost_covariances_min);
							
							time_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_i + ">," + (1./10 + time_average_norm/10) + " pigment {color rgb<" + time_covariance_norm + "," + (1-time_covariance_norm) + "," + (1-time_average_norm) + ">}}");
							cost_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						}
					}
				}
	
				for (int classes_i = 0; classes_i < classes_steps; classes_i++)
				{
					for (int samples_i = 0; samples_i < samples_steps; samples_i++)
					{
						int random_min_i = 0;
						
						for (int random_i = 0; random_i < random_steps; random_i++)
						{
							if (cost_averages[classes_i][samples_i][random_i] < cost_averages[classes_i][samples_i][random_min_i])
							{
								random_min_i = random_i;
							}
						}
	
						double cost_average = cost_averages[classes_i][samples_i][random_min_i];
						double cost_covariance = cost_covariances[classes_i][samples_i][random_min_i];
	
						double cost_average_norm = (cost_average - cost_averages_min) / (cost_averages_max - cost_averages_min);
						double cost_covariance_norm = (cost_covariance - cost_covariances_min) / (cost_covariances_max - cost_covariances_min);
	
						cost_minima_out.println("sphere {<" + classes_i + "," + random_min_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						cost_random_out.println("sphere {<" + classes_i + "," + random_min_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						
						if (random_min_i > 0)
						{
							cost_random_out.println("cylinder {<" + classes_i + ",0," + samples_i + ">,<" + classes_i + "," + random_min_i + "," + samples_i + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
						}
					}
				}
	
				for (int classes_i = 0; classes_i < classes_steps; classes_i++)
				{
					for (int random_i = 0; random_i < random_steps; random_i++)
					{
						int samples_min_i = 0;
						
						for (int samples_i = 0; samples_i < samples_steps; samples_i++)
						{
							if (cost_averages[classes_i][samples_i][random_i] < cost_averages[classes_i][samples_min_i][random_i])
							{
								samples_min_i = samples_i;
							}
						}
	
						double cost_average = cost_averages[classes_i][samples_min_i][random_i];
						double cost_covariance = cost_covariances[classes_i][samples_min_i][random_i];
	
						double cost_average_norm = (cost_average - cost_averages_min) / (cost_averages_max - cost_averages_min);
						double cost_covariance_norm = (cost_covariance - cost_covariances_min) / (cost_covariances_max - cost_covariances_min);
	
						cost_minima_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_min_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						cost_samples_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_min_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						
						if (samples_min_i > 0)
						{
							cost_samples_out.println("cylinder {<" + classes_i + "," + random_i + ",0>,<" + classes_i + "," + random_i + "," + samples_min_i + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");	
						}
					}
				}
	
				for (int random_i = 0; random_i < random_steps; random_i++)
				{
					for (int samples_i = 0; samples_i < samples_steps; samples_i++)
					{
						int classes_min_i = 0;
						
						for (int classes_i = 0; classes_i < classes_steps; classes_i++)
						{
							if (cost_averages[classes_i][samples_i][random_i] < cost_averages[classes_min_i][samples_i][random_i])
							{
								classes_min_i = classes_i;
							}
						}
	
						double cost_average = cost_averages[classes_min_i][samples_i][random_i];
						double cost_covariance = cost_covariances[classes_min_i][samples_i][random_i];
	
						double cost_average_norm = (cost_average - cost_averages_min) / (cost_averages_max - cost_averages_min);
						double cost_covariance_norm = (cost_covariance - cost_covariances_min) / (cost_covariances_max - cost_covariances_min);
	
						cost_minima_out.println("sphere {<" + classes_min_i + "," + random_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						cost_classes_out.println("sphere {<" + classes_min_i + "," + random_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + cost_covariance_norm + "," + (1-cost_covariance_norm) + "," + (1-cost_average_norm) + ">}}");
						
						if (classes_min_i > 0)
						{
							cost_classes_out.println("cylinder {<0," + random_i + "," + samples_i + ">,<" + classes_min_i + "," + random_i + "," + samples_i + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
						}
					}
				}
				
				time_out.close();
				cost_out.close();
				cost_minima_out.close();
				cost_classes_out.close();
				cost_samples_out.close();
				cost_random_out.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	protected abstract int getClasses(int classes_start, int classes_end, int classes_steps, int classes_i);
	
	protected abstract int getSamples(int samples_start, int samples_end, int samples_steps, int samples_i);
	
	protected abstract double getRandom(double random_start, double random_end, int random_steps, int random_i);
	
	private void printHeader(PrintStream out, int classes_steps, int samples_steps, int random_steps)
	{
		out.println("// header");
		
		out.println("global_settings { ambient_light rgb<3,3,3> }");
		out.println("camera { location <" + classes_steps*1.25 + "," + random_steps*1.25 + "," + samples_steps*2 + "> look_at <" + classes_steps/2. + "," + random_steps/2. + "," + samples_steps/2. + "> }");
		out.println("light_source {<" + classes_steps/2. + "," + random_steps*1.5 + "," + samples_steps*1.5 + ">,color rgb<1,1,1> area_light <10,0,0>,<0,0,10>,10,10 adaptive 3 jitter}");
		out.println("plane {<1,0,0>,-2 pigment {color rgb<1,1,1>}}");
		out.println("plane {<0,1,0>,-2 pigment {color rgb<1,1,1>}}");
		out.println("plane {<0,0,1>,-2 pigment {color rgb<1,1,1>}}");
		out.println("cylinder {<0,0,0>,<" + classes_steps + ",0,0>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
		out.println("cylinder {<0,0,0>,<0," + random_steps + ",0>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
		out.println("cylinder {<0,0,0>,<0,0," + samples_steps + ">,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
		out.println("cone {<" + classes_steps + ",0,0>,0.2,<" + (classes_steps+0.5) + ",0,0>,0 pigment {color rgb<0.5,0.5,0.5>}}");
		out.println("cone {<0," + random_steps + ",0>,0.2,<0," + (random_steps+0.5) + ",0>,0 pigment {color rgb<0.5,0.5,0.5>}}");
		out.println("cone {<0,0," + samples_steps + ">,0.2,<0,0," + (samples_steps+0.5) + ">,0 pigment {color rgb<0.5,0.5,0.5>}}");
	}
	
	/*
	private void printGrids(PrintStream out, int classes_steps, int samples_steps, int random_steps, int step)
	{
		out.println("// xy grids");
		
		printXYGrids(out, classes_steps, random_steps, samples_steps, step);
		
		out.println("// yz grids");
		
		printYZGrids(out, classes_steps, random_steps, samples_steps, step);
		
		out.println("// xz grids");
		
		printXZGrids(out, classes_steps, random_steps, samples_steps, step);
	}
	
	private void printXYGrids(PrintStream out, int x_steps, int y_steps, int z_steps, int step)
	{
		for (int z = 0; z < z_steps; z+=step)
		{
			printXYGrid(out, x_steps, y_steps, z, step);
		}
	}
	
	private void printYZGrids(PrintStream out, int x_steps, int y_steps, int z_steps, int step)
	{
		for (int x = 0; x < x_steps; x+=step)
		{
			printYZGrid(out, x, y_steps, z_steps, step);
		}
	}
	
	private void printXZGrids(PrintStream out, int x_steps, int y_steps, int z_steps, int step)
	{
		for (int y = 0; y < y_steps; y+=step)
		{
			printXZGrid(out, x_steps, y, z_steps, step);
		}
	}
	*/
	
	private void printXYGrid(PrintStream out, int x_steps, int y_steps, int z, int step)
	{
		for (int x = 0; x < x_steps; x+=step)
		{
			out.println("cylinder {<" + x + ",0," + z + ">,<" + x + "," + (y_steps-1) + "," + z + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
		for (int y = 0; y < y_steps; y+=step)
		{
			out.println("cylinder {<0," + y + "," + z + ">,<" + (x_steps-1) + "," + y + "," + z + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
	}
	
	private void printYZGrid(PrintStream out, int x, int y_steps, int z_steps, int step)
	{
		for (int y = 0; y < y_steps; y+=step)
		{
			out.println("cylinder {<" + x + "," + y + ",0>,<" + x + "," + y + "," + (z_steps-1) + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
		for (int z = 0; z < z_steps; z+=step)
		{
			out.println("cylinder {<" + x + ",0," + z + ">,<" + x + "," + (y_steps-1) + "," + z + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
	}
	
	private void printXZGrid(PrintStream out, int x_steps, int y, int z_steps, int step)
	{
		for (int x = 0; x < x_steps; x+=step)
		{
			out.println("cylinder {<" + x + "," + y + ",0>,<" + x + "," + y + "," + (z_steps-1) + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
		for (int z = 0; z < z_steps; z+=step)
		{
			out.println("cylinder {<0," + y + "," + z + ">,<" + (x_steps-1) + "," + y + "," + z + ">,0.025 pigment {color rgb<0.5,0.5,0.5>}}");
		}
	}

}
