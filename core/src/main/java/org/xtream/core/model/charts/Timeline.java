package org.xtream.core.model.charts;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Port;
import org.xtream.core.model.Reference;

public class Timeline extends Chart
{

	@Reference
	private Port<Double>[] ports;
	
	@SafeVarargs
	public Timeline(Port<Double>... ports)
	{
		this.ports = ports;
	}
	
	@Reference
	public Port<Double>[] getPorts()
	{
		return ports;
	}

}
