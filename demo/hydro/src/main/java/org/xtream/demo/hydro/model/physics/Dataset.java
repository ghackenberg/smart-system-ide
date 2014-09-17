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
		this.measurements = measurements.toArray(new double[measurements.size()][Constants.STAUSTUFE_COUNT * Constants.STAUSTUFE_MEASUREMENTS]);
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
		return measurements[timepoint][staustufe * Constants.STAUSTUFE_MEASUREMENTS + Constants.INFLOW_INDEX];
	}
	public double getLevel(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * Constants.STAUSTUFE_MEASUREMENTS + Constants.LEVEL_INDEX];
	}
	public double getOutflowTotal(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * Constants.STAUSTUFE_MEASUREMENTS + Constants.OUTFLOW_TOTAL_INDEX];
	}
	public double getOutflowTurbine(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * Constants.STAUSTUFE_MEASUREMENTS + Constants.OUTFLOW_TURBINE_INDEX];
	}
	public double getProduction(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * Constants.STAUSTUFE_MEASUREMENTS + Constants.PRODUCTION_INDEX];
	}
	
	public int getLength()
	{
		return timepoints.length;
	}

}
