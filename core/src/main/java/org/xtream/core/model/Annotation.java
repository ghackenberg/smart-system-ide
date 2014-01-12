package org.xtream.core.model;

public class Annotation<T>
{
	
	public String name;
	public String qualifiedName;
	public Port<T> port;
	
	public Annotation(Port<T> port)
	{
		this.port = port;
	}

}
