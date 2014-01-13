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
		new Engine<>(RootComponent.class).run(96, 100, 20, 0);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> costOutput = new Port<>();
	
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
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	public Equivalence temperatureEquivalence = new Equivalence(net.temperatureOutput);
	
	public Equivalence levelEquivalence = new Equivalence(net.levelOutput);
	
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
