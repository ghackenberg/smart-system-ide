package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;

public class ObjectiveComponent extends Component
{
	
	// Ports
	
	public Port<Double> priceInput = new Port<>();
	public Port<Double> productionInput = new Port<>();

	public Port<Double> rewardOutput = new Port<>();
	
	// Objectives
	
	public Objective objective = new Objective(rewardOutput, Direction.MAX);
	
	// Charts
	
	public Chart rewardChart = new Chart(rewardOutput);
	
	// Expressions
	
	public Expression<Double> rewardExpression = new Expression<Double>(rewardOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return (timepoint == 0 ? 0 : rewardOutput.get(timepoint - 1)) + productionInput.get(timepoint) * priceInput.get(timepoint);
		}
	};
}
