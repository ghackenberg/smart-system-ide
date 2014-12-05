package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageA;

public class QuadraticStageARootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new QuadraticStageARootComponent(), DURATION, SAMPLES, CLASSES, ROUNDS, RANDOMNESS, CACHING, CLUSTER_ROUNDS);
	}

	public QuadraticStageARootComponent()
	{
		super(new StageA());
	}

}
