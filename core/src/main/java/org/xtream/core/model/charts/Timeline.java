package org.xtream.core.model.charts;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class Timeline extends Chart
{

	@Reference
	private Series<Double>[] series;
	
	@SafeVarargs
	public Timeline(Series<Double>... series)
	{
		this(null, series);
	}
	@SafeVarargs
	public Timeline(String label, Series<Double>... series)
	{
		this(label, null, null, series);
	}
	@SafeVarargs
	public Timeline(String label, String domain, String range, Series<Double>... series)
	{
		super(label, domain, range);
		
		this.series = series;
	}
	
	@SafeVarargs
	public Timeline(Port<Double>... ports)
	{
		this(null, ports);
	}
	@SafeVarargs
	public Timeline(String label, Port<Double>... ports)
	{
		this(label, null, null, ports);
	}
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public Timeline(String label, String domain, String range, Port<Double>... ports)
	{
		super(label, domain, range);
		
		series = new Series[ports.length];
		
		for (int i = 0; i < series.length; i++)
		{
			series[i] = new Series<Double>(ports[i]);
		}
	}
	
	@Reference
	public Series<Double>[] getSeries()
	{
		return series;
	}

}
