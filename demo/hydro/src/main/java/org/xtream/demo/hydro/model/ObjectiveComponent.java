package org.xtream.demo.hydro.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.Preference;
import org.xtream.core.model.markers.objectives.MaxObjective;
import org.xtream.core.model.markers.preferences.MaxPreference;
import org.xtream.core.optimizer.State;

public class ObjectiveComponent extends Component
{
	
	// Ports
	
	public Port<Double> priceInput = new Port<>();
	public Port<Double> productionInput = new Port<>();

	public Port<Double> rewardOutput = new Port<>();
	
	// Objectives
	
	public Objective objective = new MaxObjective(rewardOutput);
	
	// Preferences
	
	public Preference preference = new MaxPreference(rewardOutput);
	
	// Charts
	
	public Chart rewardChart = new Timeline(rewardOutput);
	
	// Expressions
	
	public Expression<Double> rewardExpression = new Expression<Double>(rewardOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return (timepoint == 0 ? 0 : rewardOutput.get(state, timepoint - 1)) + productionInput.get(state, timepoint) * priceInput.get(state, timepoint);
		}
	};
}
