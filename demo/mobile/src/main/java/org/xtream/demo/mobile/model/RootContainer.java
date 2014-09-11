package org.xtream.demo.mobile.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.workbench.Workbench;
import org.xtream.core.workbench.parts.ComponentArchitecturePart;
import org.xtream.core.workbench.parts.ComponentChartsPart;
import org.xtream.core.workbench.parts.ComponentChildrenPart;
import org.xtream.core.workbench.parts.ComponentHierarchyPart;
import org.xtream.core.workbench.parts.EngineMonitorPart;
import org.xtream.core.workbench.parts.StateSpacePart;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.model.root.ConstraintsComponent;
import org.xtream.demo.mobile.model.root.EquivalenceComponent;
import org.xtream.demo.mobile.model.root.ModulesContainer;
import org.xtream.demo.mobile.model.root.ObjectiveComponent;
import org.xtream.demo.mobile.workbench.parts.AggregateNavigationGraphPart;

public class RootContainer extends Component
{
//	public static int SIZE = 100;
//	public static int DURATION = 50;
//	public static int SAMPLES = 50;
//	public static int CLUSTERS = 25;
//	public static double RANDOMNESS = 0;
//	public static double CACHING = 0;
	
	public static int SIZE = 5;
	public static int DURATION = 15;
	public static int SAMPLES = 10;
	public static int CLUSTERS = 50;
	public static double RANDOMNESS = 0;
	public static double CACHING = 0;
	public static int ROUNDS= 50;
	
	// vehicle specific parameters
	
	
	public static Graph graph = new Graph("graph1", "TESTMAP.xml");
	
	public static <T> void main(String[] args)
	{
		new Workbench<>(new RootContainer(), DURATION, SAMPLES, CLUSTERS, RANDOMNESS, CACHING, ROUNDS, new ComponentHierarchyPart<>(0,0,1,1), new ComponentChildrenPart<>(0,1,1,1), new ComponentArchitecturePart<>(1,0,2,1), new StateSpacePart<>(3,0,2,1), new AggregateNavigationGraphPart<>(graph,1,1,2,1), new ComponentChartsPart<>(3,1,2,1), new EngineMonitorPart<>(5,0,1,2));
	}
	
	public RootContainer() {
		modules = new ModulesContainer(SIZE, graph);
		objective = new ObjectiveComponent(modules);
		equivalences = new EquivalenceComponent(modules);
		constraints = new ConstraintsComponent(SIZE, graph, modules);
		
		costChart = new Timeline(objective.costOutput);
	}
	
	// Components
	
	public ModulesContainer modules;
	public EquivalenceComponent equivalences;
	public ObjectiveComponent objective;
	public ConstraintsComponent constraints;
	
	// Charts
	
	public Chart costChart;
}
