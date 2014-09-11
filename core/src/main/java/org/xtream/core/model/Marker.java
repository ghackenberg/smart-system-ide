package org.xtream.core.model;

public class Marker<T> extends Element
{

	@Reference
	private Port<T> port;
	
	public Marker(Port<T> port)
	{
		super(Marker.class.getClassLoader().getResource("elements/marker.png"));
		
		this.port = port;
	}
	
	@Reference
	public Port<T> getPort()
	{
		return port;
	}

}
