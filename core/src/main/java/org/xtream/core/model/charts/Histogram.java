package org.xtream.core.model.charts;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class Histogram<T> extends Chart
{

	@Reference
	private Port<T>[] ports;

	@SafeVarargs
	public Histogram(Port<T>... ports)
	{
		this(null, ports);
	}
	@SafeVarargs
	public Histogram(String label, Port<T>... ports)
	{
		this(label, null, null, ports);
	}
	@SafeVarargs
	public Histogram(String label, String domain, String range, Port<T>... ports)
	{
		super(label, domain, range);
		
		this.ports = ports;
	}
	
	@Reference
	public Port<T>[] getPorts()
	{
		return ports;
	}

}
