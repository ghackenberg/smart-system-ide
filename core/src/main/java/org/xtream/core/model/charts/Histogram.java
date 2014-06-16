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
		this.ports = ports;
	}
	
	public Port<T>[] getPorts()
	{
		return ports;
	}

}
