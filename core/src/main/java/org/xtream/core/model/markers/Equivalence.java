package org.xtream.core.model.markers;

import org.xtream.core.model.Marker;
import org.xtream.core.model.Port;

public class Equivalence extends Marker<Double>
{
	
	private double weight;

	public Equivalence(Port<Double> port)
	{
		this(port, 1);
	}
	public Equivalence(Port<Double> port, double weight)
	{
		super(port);
		
		this.weight = weight;
	}
	
	public double getWeight()
	{
		return weight;
	}

}
