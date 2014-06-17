package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.optimizer.State;

public class NetComponent extends Component
{
	
	// Ports
	
	public Port<Double> hauptkraftwerkProductionInput = new Port<>();
	public Port<Double> wehr1ProductionInput = new Port<>();
	public Port<Double> wehr2ProductionInput = new Port<>();
	public Port<Double> wehr3ProductionInput = new Port<>();
	public Port<Double> wehr4ProductionInput = new Port<>();
	
	public Port<Double> productionOutput = new Port<>();
	
	// Charts
	
	public Chart productionChart = new Timeline(productionOutput, hauptkraftwerkProductionInput, wehr1ProductionInput, wehr2ProductionInput, wehr3ProductionInput, wehr4ProductionInput);
	
	// Expressions
	
	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(State state, int timepoint)
		{
			return hauptkraftwerkProductionInput.get(state, timepoint) + wehr1ProductionInput.get(state, timepoint) + wehr2ProductionInput.get(state, timepoint) + wehr3ProductionInput.get(state, timepoint) + wehr4ProductionInput.get(state, timepoint);
		}
	};

}
