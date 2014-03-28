package org.xtream.core.model;

public class Histogram
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
