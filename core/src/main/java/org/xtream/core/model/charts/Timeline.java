package org.xtream.core.model.charts;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Timeline extends Chart
{
	
	public String name;
	public String qualifiedName;
	
	public Component parent;
	
	public Port<Double>[] ports;
	
	@SafeVarargs
	public Timeline(Port<Double>... ports)
	{
		this.ports = ports;
	}

}
