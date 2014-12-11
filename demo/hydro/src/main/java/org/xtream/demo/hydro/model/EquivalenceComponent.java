package org.xtream.demo.hydro.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.markers.Equivalence;

public class EquivalenceComponent extends Component
{
	
	// Ports
	
	public Port<Double> speicherseeLevelInput = new Port<>();
	public Port<Double> volumen1LevelInput = new Port<>();
	public Port<Double> volumen2LevelInput = new Port<>();
	public Port<Double> volumen3LevelInput = new Port<>();
	public Port<Double> volumen4LevelInput = new Port<>();
	
	// Equivalences
	
	public Equivalence speicherseeLevelEquivalence = new Equivalence(speicherseeLevelInput, 200);
	public Equivalence volumen1LevelEquivalence = new Equivalence(volumen1LevelInput);
	public Equivalence volumen2LevelEquivalence = new Equivalence(volumen2LevelInput);
	public Equivalence volumen3LevelEquivalence = new Equivalence(volumen3LevelInput);
	public Equivalence volumen4LevelEquivalence = new Equivalence(volumen4LevelInput);

}
