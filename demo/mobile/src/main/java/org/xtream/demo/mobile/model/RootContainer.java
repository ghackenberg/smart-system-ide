package org.xtream.demo.mobile.model;


import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.optimizer.beam.strategies.GridStrategy;
import org.xtream.core.workbench.Workbench;
import org.xtream.core.workbench.parts.ComponentChartsPart;
import org.xtream.core.workbench.parts.ComponentChildrenPart;
import org.xtream.core.workbench.parts.ComponentHierarchyPart;
import org.xtream.core.workbench.parts.ModelScenePart;
import org.xtream.core.workbench.parts.StateSpacePart;
import org.xtream.core.workbench.parts.charts.ClustersChartMonitorPart;
import org.xtream.core.workbench.parts.charts.MemoryChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ObjectiveChartMonitorPart;
import org.xtream.core.workbench.parts.charts.StatesChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TimeChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TracesChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ViolationChartMonitorPart;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.model.root.ConstraintsComponent;
import org.xtream.demo.mobile.model.root.EquivalenceComponent;
import org.xtream.demo.mobile.model.root.ModulesContainer;
import org.xtream.demo.mobile.model.root.ObjectiveComponent;

public class RootContainer extends Component
{
//	public static int SIZE = 100;
//	public static int DURATION = 50;
//	public static int SAMPLES = 50;
//	public static int CLUSTERS = 25;
//	public static double RANDOMNESS = 0;
//	public static double CACHING = 0;
	
	public static int SIZE = 60;
	public static int DURATION = 25;
	public static int SAMPLES = 40;
	public static int CLUSTERS = 40;
	public static int BRANCH_ROUNDS = 2;
	public static long BRANCH_DURATION = 5;
	public static int KMEANS_ROUNDS = 1000;
	public static long KMEANS_DURATION = 100;
	
	// vehicle specific parameters
	
	
	public static Graph graph = new Graph("graph1", "TESTMAP.xml");
	
	public static <T> void main(String[] args)
	{
		new Workbench<>(new Engine<>(new RootContainer(), SAMPLES, CLUSTERS, BRANCH_ROUNDS, BRANCH_DURATION, true, new GridStrategy()), DURATION, new ComponentHierarchyPart<>(0,0,1,2), new ComponentChildrenPart<>(0,2,1,2), new ViolationChartMonitorPart<>(3,0), new ModelScenePart<>(1,2,2,2), new ComponentChartsPart<>(3,2,2,2), new StatesChartMonitorPart<>(5,0), new ClustersChartMonitorPart<>(5,1), new TracesChartMonitorPart<>(3,1), new StateSpacePart<>(3,1), new ObjectiveChartMonitorPart<>(4,1), new TimeChartMonitorPart<>(5,2), new MemoryChartMonitorPart<>(5,3));
	}
	
	public RootContainer() {
		
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
