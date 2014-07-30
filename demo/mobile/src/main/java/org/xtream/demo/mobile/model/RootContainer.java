package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootContainer extends Component
{
	public static int SIZE = 100;
	public static int DURATION = 50;
	public static int SAMPLES = 50;
	public static int CLUSTERS = 50;
	public static double RANDOMNESS = 0;
	public static double CACHING = 0;

	public static Graph graph = new Graph("graph1", "TESTMAP.xml");
	
	public static void main(String[] args)
	{
		new Workbench<>(new RootContainer(), DURATION, SAMPLES, CLUSTERS, RANDOMNESS, CACHING, graph);
	}
	
	public RootContainer() {
		modules = new ModulesContainer(SIZE, graph);
		objective = new ObjectiveComponent(modules);
		equivalences = new EquivalenceComponent(modules);
		constraints = new ConstraintsComponent(SIZE, graph, modules);
		
		costs = new ChannelExpression<>(costInput, objective.costOutput);
	}
	
	// Components
	
	public ModulesContainer modules;
	public EquivalenceComponent equivalences;
	public ObjectiveComponent objective;
	public ConstraintsComponent constraints;
	
	// Outputs
	
	public Port<Double> costInput = new Port<>();
	
	// Channels
	
	public ChannelExpression<Double> costs;
	
	// Charts
	
	public Chart costChart = new Timeline(costInput);
}
