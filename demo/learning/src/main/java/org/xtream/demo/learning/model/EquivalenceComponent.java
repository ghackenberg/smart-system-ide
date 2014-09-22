package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public EquivalenceComponent(int size)
	{
		stateInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			stateInputs[i] = new Port<>();
		}
	}
	
	// Ports
	
	public Port<Double>[] stateInputs;
	public Port<Double> equivalenceOutput = new Port<>();
	
	// Markers
	
	public Marker<Double> equivalenceMarker = new Equivalence(equivalenceOutput);
	
	// Charts
	
	public Chart equivalenceChart = new Timeline(equivalenceOutput);
	
	// Expressions
	
	public Expression<Double> equivalenceExpression = new Expression<Double>(equivalenceOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (Port<Double> stateInput : stateInputs)
			{
				sum += stateInput.get(state, timepoint);
			}
			
			return sum;
		}
	};

}
