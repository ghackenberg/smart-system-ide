package org.xtream.demo.thermal.model.objective;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.stage.StageA;

public class QuadraticStageARootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(QuadraticStageARootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS);
	}

	public QuadraticStageARootComponent()
	{
		super(new StageA());
	}

}
