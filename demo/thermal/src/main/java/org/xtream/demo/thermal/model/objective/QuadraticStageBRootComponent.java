package org.xtream.demo.thermal.model.objective;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.stage.StageB;

public class QuadraticStageBRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageBRootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS);
	}

	public QuadraticStageBRootComponent()
	{
		super(new StageB());
	}

}
