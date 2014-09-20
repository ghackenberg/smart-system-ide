package org.xtream.core.model;

public abstract class Chart extends Element
{
	
	private String label;
	private String domain;
	private String range;
	
	public Chart()
	{
		this(null);
	}
	public Chart(String label)
	{
		this(label, null, null);
	}
	public Chart(String label, String domain, String range)
	{
		super(Chart.class.getClassLoader().getResource("elements/chart.png"));
		
		this.label = label;
		this.setDomain(domain);
		this.setRange(range);
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	public String getLabel()
	{
		return label;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}
	public String getDomain()
	{
		return domain;
	}

	public void setRange(String range)
	{
		this.range = range;
	}
	public String getRange()
	{
		return range;
	}

}
