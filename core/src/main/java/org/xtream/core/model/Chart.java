package org.xtream.core.model;

public abstract class Chart extends Element
{
	
	public Chart()
	{
		super(Chart.class.getClassLoader().getResource("chart.png"));
	}

}
