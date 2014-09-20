package org.xtream.core.model.charts;

import org.xtream.core.model.Port;

public class Series<T>
{
	
	private Port<T> port;
	private String label;
	
	public Series(Port<T> port)
	{
		this(null, port);
	}
	public Series(String label, Port<T> port)
	{
		this.setPort(port);
		this.setLabel(label);
	}
	
	public Port<T> getPort()
	{
		return port;
	}
	public void setPort(Port<T> port)
	{
		this.port = port;
	}
	
	public String getLabel()
	{
		if (label != null)
		{
			return label;
		}
		else
		{
			return port.getQualifiedName();
		}
	}
	public void setLabel(String label)
	{
		this.label = label;
	}

}
