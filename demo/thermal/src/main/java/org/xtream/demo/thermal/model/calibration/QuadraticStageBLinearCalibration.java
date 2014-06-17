package org.xtream.demo.thermal.model.calibration;

import org.xtream.core.optimizer.calibrators.LinearCalibrator;
import org.xtream.demo.thermal.model.configuration.QuadraticStageBRootComponent;

public class QuadraticStageBLinearCalibration
{
	
	public static int PROCESSORS = Runtime.getRuntime().availableProcessors() - 2;
	
	public static int DURATION = 96;
	
	public static int CLUSTERS_START = 2;
	public static int CLUSTERS_END = 10;
	public static int CLUSTERS_STEPS = 5;
	
	public static int SAMPLES_START = 2;
	public static int SAMPLES_END = 10;
	public static int SAMPLES_STEPS = 5;
	
	public static double RANDOM_START = 0.0;
	public static double RANDOM_END = 1.0;
	public static int RANDOM_STEPS = 2;
	
	public static int ITERATIONS = 10;
	
	public static void main(String[] args)
	{
		new LinearCalibrator<>(new QuadraticStageBRootComponent()).run(PROCESSORS, DURATION, CLUSTERS_START, CLUSTERS_END, CLUSTERS_STEPS, SAMPLES_START, SAMPLES_END, SAMPLES_STEPS, RANDOM_START, RANDOM_END, RANDOM_STEPS, ITERATIONS);
	}

}
