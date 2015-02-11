package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	
	// Constructors
	
	public EquivalenceComponent()
	{
		speicherseeLevelEquivalence = new Equivalence(speicherseeLevelInput, 5);
		
		if (Constants.STRATEGY != 1) // do not use for grid strategy
		{
			//volumen1LevelEquivalence = new Equivalence(volumen1LevelInput);
			//volumen2LevelEquivalence = new Equivalence(volumen2LevelInput);
			//volumen3LevelEquivalence = new Equivalence(volumen3LevelInput);
			//volumen4LevelEquivalence = new Equivalence(volumen4LevelInput, 2);
		}
		else
		{
			//randomEquivalence = new Equivalence(randomOutput);
		}
	}
	
	// Ports
	
	public Port<Double> speicherseeLevelInput = new Port<>();
	public Port<Double> volumen1LevelInput = new Port<>();
	public Port<Double> volumen2LevelInput = new Port<>();
	public Port<Double> volumen3LevelInput = new Port<>();
	public Port<Double> volumen4LevelInput = new Port<>();
	
	public Port<Double> randomOutput = new Port<>();
	
	// Equivalences
	
	public Equivalence speicherseeLevelEquivalence;
	public Equivalence volumen1LevelEquivalence;
	public Equivalence volumen2LevelEquivalence;
	public Equivalence volumen3LevelEquivalence;
	public Equivalence volumen4LevelEquivalence;
	public Equivalence randomEquivalence;
	
	// Expressions
	
	public Expression<Double> randomExpression = new Expression<Double>(randomOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return Math.random();
		}
	};

}
