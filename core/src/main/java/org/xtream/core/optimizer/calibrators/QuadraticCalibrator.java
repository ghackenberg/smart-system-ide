package org.xtream.core.optimizer.calibrators;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Calibrator;

public class QuadraticCalibrator<T extends Component> extends Calibrator<T>
{

	public QuadraticCalibrator(Class<T> type)
	{
		super(type);
	}
	
	@Override
	protected int getClasses(int classes_start, int classes_end, int classes_steps, int classes_i)
	{
		int difference = classes_end - (classes_start - 1);
		
		double increment = Math.pow(difference, 1./(classes_steps - 1));
		
		return (int) Math.ceil(classes_start - 1 + Math.pow(increment, classes_i));
	}

	@Override
	protected int getSamples(int samples_start, int samples_end, int samples_steps, int samples_i)
	{
		int difference = samples_end - (samples_start - 1);
		
		double increment = Math.pow(difference, 1./(samples_steps - 1));
		
		return (int) Math.ceil(samples_start - 1 + Math.pow(increment, samples_i));
	}

	@Override
	protected double getRandom(double random_start, double random_end, int random_steps, int random_i)
	{
		double difference = random_end - random_start;
		
		double increment = Math.pow(difference, 1./(random_steps - 1));
		
		return (int) (random_start + random_i == 0 ? 0 : Math.pow(increment, random_i));
	}

}
