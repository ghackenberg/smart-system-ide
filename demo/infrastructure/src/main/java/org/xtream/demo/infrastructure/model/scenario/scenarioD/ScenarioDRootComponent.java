package org.xtream.demo.infrastructure.model.scenario.scenarioD;

import org.xtream.core.workbench.Workbench;
import org.xtream.core.workbench.parts.ComponentChartsPart;
import org.xtream.core.workbench.parts.ComponentChildrenPart;
import org.xtream.core.workbench.parts.ComponentHierarchyPart;
import org.xtream.core.workbench.parts.ModelScenePart;
import org.xtream.core.workbench.parts.StateSpacePart;
import org.xtream.core.workbench.parts.charts.ClusterChartMonitorPart;
import org.xtream.core.workbench.parts.charts.MemoryChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ObjectiveChartMonitorPart;
import org.xtream.core.workbench.parts.charts.StateChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TimeChartMonitorPart;
import org.xtream.core.workbench.parts.charts.TraceChartMonitorPart;
import org.xtream.core.workbench.parts.charts.ViolationChartMonitorPart;
import org.xtream.demo.infrastructure.model.RootComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;

public class ScenarioDRootComponent extends RootComponent {
	
	public ScenarioDRootComponent() 
	{
		super(new ScenarioD());
	}
	
	public static <T> void main(String[] args)
	{
		new Workbench<>(new ScenarioDRootComponent(), Scenario.DURATION, Scenario.SAMPLES, Scenario.CLUSTERS, Scenario.RANDOMNESS, Scenario.CACHING, Scenario.ROUNDS, true,  new ComponentHierarchyPart<>(0,0,1,2), new ComponentChildrenPart<>(0,2,1,2), new ViolationChartMonitorPart<>(3,0), new ModelScenePart<>(1,2,2,2), new ComponentChartsPart<>(3,2,2,2), new StateChartMonitorPart<>(5,0), new ClusterChartMonitorPart<>(5,1), new TraceChartMonitorPart<>(3,1), new StateSpacePart<>(3,1), new ObjectiveChartMonitorPart<>(4,1), new TimeChartMonitorPart<>(5,2), new MemoryChartMonitorPart<>(5,3));
	}
	
}
