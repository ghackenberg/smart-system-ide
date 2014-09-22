package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Histogram;
import org.xtream.core.model.markers.Constraint;

public class ConstraintComponent extends Component
{
	
	// Ports
	
	public Port<Double> stateInput = new Port<>();
	public Port<Boolean> validOutput = new Port<>();
	
	// Markers
	
	public Marker<Boolean> validMarker = new Constraint(validOutput);
	
	// Charts
	
	public Chart validChart = new Histogram<>(validOutput);
	
	// Expressions
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return Math.abs(stateInput.get(state, timepoint)) <= 5;
		}
	};

}
