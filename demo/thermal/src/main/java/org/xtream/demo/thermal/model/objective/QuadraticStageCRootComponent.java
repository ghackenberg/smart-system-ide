package org.xtream.demo.thermal.model.objective;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.stage.StageC;

public class QuadraticStageCRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageCRootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS);
	}

	public QuadraticStageCRootComponent()
	{
		super(new StageC());
	}

}
