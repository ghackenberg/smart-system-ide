package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageA;

public class QuadraticStageARootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageARootComponent.class, DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public QuadraticStageARootComponent()
	{
		super(new StageA());
	}

}
