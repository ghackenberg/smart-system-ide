package org.xtream.core.model;

public class Chart
{
	
	public String name;
	public String qualifiedName;
	
	public Port<Double>[] ports;
	
	@SafeVarargs
	public Chart(Port<Double>... ports)
	{
		this.ports = ports;
	}

}
