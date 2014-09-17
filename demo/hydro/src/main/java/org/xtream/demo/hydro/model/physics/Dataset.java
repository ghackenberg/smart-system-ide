package org.xtream.demo.hydro.model.physics;

import java.util.List;

public class Dataset
{
	
	private String[] timepoints;
	private double[][] measurements;
	
	public Dataset(List<String> timepoints, List<double[]> measurements)
	{
		if (timepoints.size() != measurements.size())
		{
			throw new IllegalArgumentException("List sizes must be equal! (" + timepoints.size() + " vs " + measurements.size() + ")");
		}
		
		this.timepoints = timepoints.toArray(new String[timepoints.size()]);
		this.measurements = measurements.toArray(new double[measurements.size()][15]);
	}
	
	public String[] getTimepoints()
	{
		return timepoints;
	}
	public double[][] getMeasurements()
	{
		return measurements;
	}
	
	public String getTimepoint(int timepoint)
	{
		return timepoints[timepoint];
	}
	public double getInflow(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * 3];
	}
	public double getLevel(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * 3 + 1];
	}
	public double getOutflow(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * 3 + 2];
	}
	
	public int getLength()
	{
		return timepoints.length;
	}

}
