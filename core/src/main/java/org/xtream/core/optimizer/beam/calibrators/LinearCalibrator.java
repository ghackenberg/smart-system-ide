package org.xtream.core.optimizer.beam.calibrators;

import org.xtream.core.model.containers.Component;
import org.xtream.core.optimizer.beam.Calibrator;

public class LinearCalibrator<T extends Component> extends Calibrator<T>
{

	public LinearCalibrator(T root)
	{
		super(root);
	}
	
	@Override
	protected int getClasses(int classes_start, int classes_end, int classes_steps, int classes_i)
	{
		return (int) (classes_start + (classes_end - classes_start) *  classes_i / (classes_steps - 1.));
	}

	@Override
	protected int getSamples(int samples_start, int samples_end, int samples_steps, int samples_i)
	{
		return (int) (samples_start + (samples_end - samples_start) *  samples_i / (samples_steps - 1.));
	}

	@Override
	protected double getRandom(double random_start, double random_end, int random_steps, int random_i)
	{
		return random_start + (random_end - random_start) *  random_i / (random_steps - 1.);
	}

}
