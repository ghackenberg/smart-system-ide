package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.LinearRootComponent;
import org.xtream.demo.thermal.model.stage.StageB;

public class LinearStageBRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new LinearStageBRootComponent(), DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public LinearStageBRootComponent()
	{
		super(new StageB());
	}

}
