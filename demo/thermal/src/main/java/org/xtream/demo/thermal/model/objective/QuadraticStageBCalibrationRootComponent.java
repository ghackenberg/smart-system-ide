package org.xtream.demo.thermal.model.objective;

import org.xtream.core.optimizer.Calibrator;
import org.xtream.demo.thermal.model.stage.StageB;

public class QuadraticStageBCalibrationRootComponent extends LinearRootComponent
{
	
	public static int STEPS = 5;
	public static int ITERATIONS = 10;
	
	public static void main(String[] args)
	{
		new Calibrator<>(QuadraticStageBCalibrationRootComponent.class).run(96, 1, 5, STEPS, 1, 5, STEPS, 0., 1., STEPS, ITERATIONS);
	}

	public QuadraticStageBCalibrationRootComponent()
	{
		super(new StageB());
	}

}
