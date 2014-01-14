package org.xtream.core.model;

public class Chart
{
	
	public String name;
	public String qualifiedName;
	
	public Component parent;
	
	public Port<Double>[] ports;
	
	@SafeVarargs
	public Chart(Port<Double>... ports)
	{
		this.ports = ports;
	}

}
