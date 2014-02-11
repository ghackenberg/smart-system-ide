package org.xtream.demo.thermal.model.objective;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.stage.StageC;

public class LinearStageCRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(LinearStageCRootComponent.class, DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public LinearStageCRootComponent()
	{
		super(new StageC());
	}

}
