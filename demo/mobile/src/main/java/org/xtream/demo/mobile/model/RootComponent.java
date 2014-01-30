package org.xtream.demo.mobile.model;

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
	
	public static void main(String[] args)
	{
		new Workbench<>(RootComponent.class, 96, 10, 10, 10.);
	}
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	
	public Port<Double> speedOutput = new Port<>();
	
	// Components
	
	public OverallSystemComponent overallSystem = new OverallSystemComponent(1000000., 3);
	
	// Equivalences
	
	public Equivalence speedEquivalance = new Equivalence(speedOutput);
	
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
	
	public Expression<Double> speedExpression = new Expression<Double>(speedOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double sum = 0.;
			
			return sum;
		}
	};
	
}
