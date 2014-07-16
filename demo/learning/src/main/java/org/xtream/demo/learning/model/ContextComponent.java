package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;

public class ContextComponent extends Component
{
	
	// Ports
	
	public Port<Boolean> decisionInput = new Port<>();
	public Port<Double> stateOutput = new Port<>();
	
	// Charts
	
	public Chart stateChart = new Timeline(stateOutput);
	
	// Expressions
	
	public Expression<Double> stateExpression = new Expression<Double>(stateOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			if (timepoint == 0)
			{
				return 0.;
			}
			else
			{
				return stateOutput.get(state, timepoint - 1) + (decisionInput.get(state, timepoint) ? 1 : -1);
			}
		}
	};

}
