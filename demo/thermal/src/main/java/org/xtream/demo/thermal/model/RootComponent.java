package org.xtream.demo.thermal.model;

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
		new Workbench<>(RootComponent.class, 96, 100, 10, 0.);
	}
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> levelOutput = new Port<>();
	
	// Components
	
	public NetComponent net = new NetComponent(4);
	
	// Equivalences
	
	public Equivalence temperatureEquivalence = new Equivalence(temperatureOutput);
	public Equivalence levelEquivalence = new Equivalence(levelOutput);
	
	// Objectives
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	// Charts
	
	public Chart costChart = new Chart(costOutput);
	public Chart temperatureChart = new Chart(temperatureOutput);
	public Chart levelChart = new Chart(levelOutput);
	
	// Previews
	
	public Chart costPreview = new Chart(costOutput);
	
	// Expressions
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += net.balanceOutput.get(timepoint) * net.balanceOutput.get(timepoint);
		}
	};
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double average = 0.;
			
			for (ThermalComponent thermal : net.modules.thermals)
			{
				average += thermal.physics.temperatureOutput.get(timepoint) / net.modules.thermals.length;
			}
			
			return average;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return net.modules.storage.physics.levelOutput.get(timepoint);
		}
		
	};

}
