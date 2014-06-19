package org.xtream.core.model;

public class Marker<T> extends Element
{

	@Reference
	private Port<T> port;
	
	public Marker(Port<T> port)
	{
		super(Marker.class.getClassLoader().getResource("marker.png"));
		
		this.port = port;
	}
	
	public Port<T> getPort()
	{
		return port;
	}

}
