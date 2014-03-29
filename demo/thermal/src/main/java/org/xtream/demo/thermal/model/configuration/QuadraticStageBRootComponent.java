package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageB;

public class QuadraticStageBRootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageBRootComponent.class, DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public QuadraticStageBRootComponent()
	{
		super(new StageB());
	}

}
