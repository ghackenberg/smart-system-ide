package org.xtream.demo.mobile.model;

import javax.swing.JFrame;

import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.optimizer.calibrators.LinearCalibrator;
import org.xtream.core.workbench.EvaluationWorkbench;

public class RootComponent extends Component
{
	public static int DURATION = 96;
	public static int COVERAGE = 5;
	public static int CLASSES = 25;
	public static double RANDOMNESS = 0.0;

	// Graph
	
	//public static Graph graph = new Graph("graph1", "map.xml");
	
	// differing heights and distances
	public static Graph graph = new Graph("graph1", "mapBigDisproportional.xml");
	
	// equal routes
	//public static Graph graph = new Graph("graph1", "mapConstant.xml");
	
	
	public static void main(String[] args)
	{
		
		EvaluationWorkbench<RootComponent> workbench = null;

		while (true)
		{
			if (workbench == null)
			{
				workbench = new EvaluationWorkbench<>(RootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS, graph);
			}
			
			if (workbench.getTimepoint() <= 95)
			{
				workbench = new EvaluationWorkbench<>(RootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS, graph);
			}
			else {
				break;
			}
		}
		
	}
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	
	// Components
	
	public OverallSystemComponent overallSystem = new OverallSystemComponent(10, graph);
	
	// Objectives
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	// Charts
	
	public Chart costChart = new Chart(costOutput);
	
	// Previews
	
	public Chart costPreview = new Chart(costOutput);

	// Expressions
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += overallSystem.costsOutput.get(timepoint);
		}
	};
	
}
