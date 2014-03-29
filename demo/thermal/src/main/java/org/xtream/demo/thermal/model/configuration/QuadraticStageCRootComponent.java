package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageC;

public class QuadraticStageCRootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageCRootComponent.class, DURATION, SAMPLES, CLASSES, RANDOMNESS);
	}

	public QuadraticStageCRootComponent()
	{
		super(new StageC());
	}

}
