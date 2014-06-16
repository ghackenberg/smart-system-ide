package org.xtream.core.model.charts;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Histogram extends Chart
{
	
	public String name;
	public String qualifiedName;
	
	public Component parent;
	
	public Port<?>[] ports;
	
	@SafeVarargs
	public Histogram(Port<?>... ports)
	{
		this.ports = ports;
	}

}
