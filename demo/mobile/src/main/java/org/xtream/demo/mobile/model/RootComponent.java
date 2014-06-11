package org.xtream.demo.mobile.model;

import org.xtream.core.datatypes.Graph;
import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	public static int DURATION = 120;
	public static int COVERAGE = 15;
	public static int CLASSES = 40;
	public static double RANDOMNESS = 0.0;

	// Graph
	
	// differing heights and distances
	public static Graph graph = new Graph("graph1", "TESTMAP.xml");
	
	public static void main(String[] args)
	{
		
		new Workbench<>(RootComponent.class, DURATION, COVERAGE, CLASSES, RANDOMNESS, graph);
		
	}
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> equivalenceOutput = new Port<>();
	
	// Components
	
	public OverallSystemComponent overallSystem = new OverallSystemComponent(120, graph);
	
	// Objectives
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	// Equivalences
	
	public Equivalence equivalence = new Equivalence(equivalenceOutput);
	
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
	
	public Expression<Double> equivalenceExpression = new Expression<Double>(equivalenceOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double sum = 0;
			
			for (Component iterator : overallSystem.modules.modules)
			{
				VehicleComponent vehicle = (VehicleComponent) iterator;
				
				sum += vehicle.physics.chargeStateRelativeOutput.get(timepoint);
			}
			
			return sum;
		}
	};
	
}
