package org.xtream.demo.basic.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.optimizer.State;

public class IntegrateComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> input = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> output = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	/* none */
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////

	public Expression<Double> outputExpression = new Expression<Double>(output)
	{
		@Override public Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : output.get(state, timepoint - 1)) + input.get(state, timepoint);
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	/* none */
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	public Objective outputObjective = new MinObjective(output);
	
	////////////
	// CHARTS //
	////////////
	
	public Chart outputChart = new Timeline(output); 
	
}
