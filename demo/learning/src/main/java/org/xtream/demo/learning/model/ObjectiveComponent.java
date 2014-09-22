package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.objectives.MinObjective;

public class ObjectiveComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public ObjectiveComponent(int size)
	{
		stateInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			stateInputs[i] = new Port<>();
		}
	}
	
	// Ports
	
	public Port<Double>[] stateInputs;
	public Port<Double> costOutput = new Port<>();
	
	// Markers
	
	public Marker<Double> costMarker = new MinObjective(costOutput);
	
	// Charts
	
	public Chart costChart = new Timeline(costOutput);
	
	// Expressions
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (Port<Double> stateInput : stateInputs)
			{
				sum += stateInput.get(state, timepoint);
			}
			
			return (timepoint == 0 ? 0 : costOutput.get(state, timepoint - 1)) + Math.abs(sum);
		}
	};

}
