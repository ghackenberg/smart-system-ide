package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.optimizer.beam.Strategy;
import org.xtream.core.optimizer.beam.strategies.KMeansStrategy;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.LinearRootComponent;
import org.xtream.demo.thermal.model.stage.StageB;

public class LinearStageBRootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		Strategy strategy = new KMeansStrategy(CLUSTER_ROUNDS, CLUSTER_DURATION);
		
		Engine<LinearStageBRootComponent> engine = new Engine<>(new LinearStageBRootComponent(), SAMPLES, CLUSTERS, BRANCH_ROUNDS, BRANCH_DURATION, RANDOMNESS, PRUNE, strategy);
		
		new Workbench<>(engine, DURATION);
	}

	public LinearStageBRootComponent()
	{
		super(new StageB());
	}

}
