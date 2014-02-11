package org.xtream.demo.thermal.model.objective;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.stage.StageA;

public class LinearStageARootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(LinearStageARootComponent.class, DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public LinearStageARootComponent()
	{
		super(new StageA());
	}

}
