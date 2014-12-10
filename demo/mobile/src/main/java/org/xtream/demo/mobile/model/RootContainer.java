package org.xtream.demo.mobile.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.workbench.Workbench;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.model.root.ConstraintsComponent;
import org.xtream.demo.mobile.model.root.EquivalenceComponent;
import org.xtream.demo.mobile.model.root.ModulesContainer;
import org.xtream.demo.mobile.model.root.ObjectiveComponent;

public class RootContainer extends Component
{
	
	// Engine parameters
	
	public static int SIZE = 100;
	public static int DURATION = 50;
	public static int SAMPLES = 50;
	public static int CLUSTERS = 50;
	public static int BRANCH_ROUNDS = 2;
	public static int BRANCH_DURATION = 5;
	public static int CLUSTER_ROUNDS = 50;
	public static int CLUSTER_DURATION = 750;
	
	// Problem parameters
	
	public static Graph graph = new Graph("graph1", "TESTMAP.xml");
	
	// Main
	
	public static <T> void main(String[] args)
	{
		new Workbench<>(new Engine<>(new RootContainer(), SAMPLES, CLUSTERS, BRANCH_ROUNDS, BRANCH_DURATION), DURATION);
	}
	
	// Constructor
	
	public RootContainer()
	{	
		modules = new ModulesContainer(SIZE, graph);
		objective = new ObjectiveComponent(modules);
		equivalences = new EquivalenceComponent(modules);
		constraints = new ConstraintsComponent(SIZE, graph, modules);
		integrate = new SceneComponent(graph, modules);
		
		costChart = new Timeline(objective.costOutput);
	}
	
	// Components
	
	public ModulesContainer modules;
	public EquivalenceComponent equivalences;
	public ObjectiveComponent objective;
	public ConstraintsComponent constraints;
	public SceneComponent integrate;
	
	// Charts
	
	public Chart costChart;
	
}
