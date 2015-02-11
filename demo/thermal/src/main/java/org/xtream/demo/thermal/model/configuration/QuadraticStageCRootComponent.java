package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.optimizer.beam.Strategy;
import org.xtream.core.optimizer.beam.strategies.KMeansStrategy;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageC;

public class QuadraticStageCRootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		Strategy strategy = new KMeansStrategy(CLUSTER_ROUNDS, CLUSTER_DURATION);
		
		Engine<QuadraticStageCRootComponent> engine = new Engine<>(new QuadraticStageCRootComponent(), SAMPLES, CLUSTERS, BRANCH_ROUNDS, BRANCH_DURATION, PRUNE, strategy);
		
		new Workbench<>(engine, DURATION);
	}

	public QuadraticStageCRootComponent()
	{
		super(new StageC());
	}

}
