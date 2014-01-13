package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.optimizer.Engine;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Engine<>(RootComponent.class).run(96, 200, 0);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> costOutput = new Port<>();
	
	public Port<Double> temperatureOutput = new Port<>();
	
	public Port<Double> levelOutput = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public NetComponent net = new NetComponent(new NetComponent(2), new NetComponent(2));
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
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
			return Math.floor((net.temperatureOutput.get(timepoint) - 2.) / 8. * 5.);
		}
	};
	
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return Math.floor(net.levelOutput.get(timepoint) / (2. * 4000. + 2. * 4000.) * 5.);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	public Equivalence<Double> temperatureEquivalence = new Equivalence<>(temperatureOutput);
	
	public Equivalence<Double> levelEquivalence = new Equivalence<>(levelOutput);
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	////////////
	// CHARTS //
	////////////
	
	public Chart costChart = new Chart(costOutput);
	
	//////////////
	// PREVIEWS //
	//////////////
	
	public Chart costPreview = new Chart(costOutput);

}
