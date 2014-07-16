package org.xtream.demo.hydro.model;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	
	// Ports
	
	public Port<Double> speicherseeLevelInput = new Port<>();
	public Port<Double> volumen1LevelInput = new Port<>();
	public Port<Double> volumen2LevelInput = new Port<>();
	public Port<Double> volumen3LevelInput = new Port<>();
	public Port<Double> volumen4LevelInput = new Port<>();
	
	public Port<Double> volumenAverageOutput = new Port<>();
	
	// Equivalences
	
	public Equivalence volumenAverageEquivalence = new Equivalence(volumenAverageOutput);
	
	// Expressions
	
	public Expression<Double> volumenAverageExpression = new Expression<Double>(volumenAverageOutput, true)
	{		@Override protected Double evaluate(State state, int timepoint)
		{
			return speicherseeLevelInput.get(state, timepoint) / Constants.SPEICHERSEE_LEVEL_MAX / 5 + volumen1LevelInput.get(state, timepoint) / Constants.VOLUMEN1_LEVEL_MAX / 5 + volumen2LevelInput.get(state, timepoint) / Constants.VOLUMEN2_LEVEL_MAX / 5 + volumen3LevelInput.get(state, timepoint) / Constants.VOLUMEN3_LEVEL_MAX / 5 + volumen4LevelInput.get(state, timepoint) / Constants.VOLUMEN4_LEVEL_MAX / 5;
		}
	};

}
