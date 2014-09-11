package org.xtream.demo.hydro.model;

import org.xtream.core.model.Port;
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
	
	/*
	public Port<Double> volumenAverageOutput = new Port<>();
	*/
	
	// Equivalences
	
	/*
	public Equivalence volumenAverageEquivalence = new Equivalence(volumenAverageOutput);
	*/
	public Equivalence speicherseeLevelEquivalence = new Equivalence(speicherseeLevelInput);
	public Equivalence volumen1LevelEquivalence = new Equivalence(volumen1LevelInput);
	public Equivalence volumen2LevelEquivalence = new Equivalence(volumen2LevelInput);
	public Equivalence volumen3LevelEquivalence = new Equivalence(volumen3LevelInput);
	public Equivalence volumen4LevelEquivalence = new Equivalence(volumen4LevelInput);
	
	// Expressions
	
	/*
	public Expression<Double> volumenAverageExpression = new Expression<Double>(volumenAverageOutput, true)
	{		@Override protected Double evaluate(State state, int timepoint)
		{
			return speicherseeLevelInput.get(state, timepoint) / Constants.SPEICHERSEE_LEVEL_MAX / 5 + volumen1LevelInput.get(state, timepoint) / Constants.VOLUMEN1_LEVEL_MAX / 5 + volumen2LevelInput.get(state, timepoint) / Constants.VOLUMEN2_LEVEL_MAX / 5 + volumen3LevelInput.get(state, timepoint) / Constants.VOLUMEN3_LEVEL_MAX / 5 + volumen4LevelInput.get(state, timepoint) / Constants.VOLUMEN4_LEVEL_MAX / 5;
		}
	};
	*/

}
