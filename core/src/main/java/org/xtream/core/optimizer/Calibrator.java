package org.xtream.core.optimizer;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.monitors.CalibrationMonitor;
import org.xtream.core.optimizer.printers.CompositePrinter;
import org.xtream.core.optimizer.viewers.CompositeViewer;

public class Calibrator<T extends Component>
{
	
	public Engine<T> engine;
	
	public Calibrator(Class<T> type)
	{
		engine = new Engine<>(type);
	}
	
	public void run(int duration, int classes_start, int classes_end, int classes_steps, int samples_start, int samples_end, int samples_steps, double random_start, double random_end, int random_steps, int iterations)
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
				int classes = (int) (classes_start + (classes_end - classes_start) *  classes_i / (classes_steps - 1.));
				
				for (int samples_i = 0; samples_i < samples_steps; samples_i++)
				{
					int samples = (int) (samples_start + (samples_end - samples_start) *  samples_i / (samples_steps - 1.));
					
					for (int random_i = 0; random_i < random_steps; random_i++)
					{
						System.gc();

						double random = random_start + (random_end - random_start) *  random_i / (random_steps - 1.);
						
						System.out.println("Generating sample " + classes + "/" + samples + "/" + random);
						
						double[] times = new double[iterations];
						double[] costs = new double[iterations]; 
						
						for (int iteration = 0; iteration < iterations; iteration++)
						{	
							System.out.print(iteration + " ");
							
							CalibrationMonitor monitor = new CalibrationMonitor();
							
							engine.run(duration, samples, classes, random, new CompositeViewer<T>(), monitor, new CompositePrinter<T>());
							
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
			
			PrintStream time_out = new PrintStream("Time.pov");
			PrintStream cost_out = new PrintStream("Cost.pov");
			
			time_out.println("global_settings { ambient_light rgb<3,3,3> }");
			time_out.println("camera { location <" + classes_steps*1.25 + "," + random_steps*1.25 + "," + samples_steps*2 + "> look_at <" + classes_steps/2. + "," + random_steps/2. + "," + samples_steps/2. + "> }");
			time_out.println("light_source {<" + classes_steps/2. + "," + random_steps*1.5 + "," + samples_steps*1.5 + ">,color rgb<1,1,1> area_light <10,0,0>,<0,0,10>,10,10 adaptive 3 jitter}");
			time_out.println("plane {<0,1,0>,-2 pigment {color rgb<1,1,1>}}");
			time_out.println("cylinder {<-1,-1,-1>,<" + classes_steps + ",-1,-1>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			time_out.println("cylinder {<-1,-1,-1>,<-1," + random_steps + ",-1>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			time_out.println("cylinder {<-1,-1,-1>,<-1,-1," + samples_steps + ">,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			time_out.println("cone {<" + classes_steps + ",-1,-1>,0.2,<" + (classes_steps+0.5) + ",-1,-1>,0 pigment {color rgb<0.5,0.5,0.5>}}");
			time_out.println("cone {<-1," + random_steps + ",-1>,0.2,<-1," + (random_steps+0.5) + ",-1>,0 pigment {color rgb<0.5,0.5,0.5>}}");
			time_out.println("cone {<-1,-1," + samples_steps + ">,0.2,<-1,-1," + (samples_steps+0.5) + ">,0 pigment {color rgb<0.5,0.5,0.5>}}");

			cost_out.println("global_settings { ambient_light rgb<3,3,3> }");
			cost_out.println("camera { location <" + classes_steps*1.25 + "," + random_steps*1.25 + "," + samples_steps*2 + "> look_at <" + classes_steps/2. + "," + random_steps/2. + "," + samples_steps/2. + "> }");
			cost_out.println("light_source {<" + classes_steps/2. + "," + random_steps*1.5 + "," + samples_steps*1.5 + ">,color rgb<1,1,1> area_light <10,0,0>,<0,0,10>,10,10 adaptive 3 jitter}");
			cost_out.println("plane {<0,1,0>,-2 pigment {color rgb<1,1,1>}}");
			cost_out.println("cylinder {<-1,-1,-1>,<" + classes_steps + ",-1,-1>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			cost_out.println("cylinder {<-1,-1,-1>,<-1," + random_steps + ",-1>,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			cost_out.println("cylinder {<-1,-1,-1>,<-1,-1," + samples_steps + ">,0.05 pigment {color rgb<0.5,0.5,0.5>}}");
			cost_out.println("cone {<" + classes_steps + ",-1,-1>,0.2,<" + (classes_steps+0.5) + ",-1,-1>,0 pigment {color rgb<0.5,0.5,0.5>}}");
			cost_out.println("cone {<-1," + random_steps + ",-1>,0.2,<-1," + (random_steps+0.5) + ",-1>,0 pigment {color rgb<0.5,0.5,0.5>}}");
			cost_out.println("cone {<-1,-1," + samples_steps + ">,0.2,<-1,-1," + (samples_steps+0.5) + ">,0 pigment {color rgb<0.5,0.5,0.5>}}");
			
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
						
						time_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_i + ">," + (1./10 + time_average_norm/10) + " pigment {color rgb<" + (0.5 + 0.5 * time_covariance_norm) + ",0.5,0.5>}}");
						cost_out.println("sphere {<" + classes_i + "," + random_i + "," + samples_i + ">," + (1./10 + cost_average_norm/10) + " pigment {color rgb<" + (0.5 + 0.5 * cost_covariance_norm) + ",0.5,0.5>}}");
					}
				}
			}
			
			time_out.close();
			cost_out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

}
