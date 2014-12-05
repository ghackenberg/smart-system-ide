package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.LinearRootComponent;
import org.xtream.demo.thermal.model.stage.StageC;

public class LinearStageCRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new LinearStageCRootComponent(), DURATION, SAMPLES, CLASSES, ROUNDS, RANDOMNESS, CACHING, CLUSTER_ROUNDS);
	}

	public LinearStageCRootComponent()
	{
		super(new StageC());
	}

}
