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
		this.measurements = measurements.toArray(new double[measurements.size()][DatasetConstants.STAUSTUFE_COUNT * DatasetConstants.STAUSTUFE_MEASUREMENTS + 3]);
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
		return measurements[timepoint][staustufe * DatasetConstants.STAUSTUFE_MEASUREMENTS + DatasetConstants.INFLOW_INDEX];
	}
	public double getLevel(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * DatasetConstants.STAUSTUFE_MEASUREMENTS + DatasetConstants.LEVEL_INDEX];
	}
	public double getOutflowTotal(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * DatasetConstants.STAUSTUFE_MEASUREMENTS + DatasetConstants.OUTFLOW_TOTAL_INDEX];
	}
	public double getOutflowTurbine(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * DatasetConstants.STAUSTUFE_MEASUREMENTS + DatasetConstants.OUTFLOW_TURBINE_INDEX];
	}
	public double getProduction(int staustufe, int timepoint)
	{
		return measurements[timepoint][staustufe * DatasetConstants.STAUSTUFE_MEASUREMENTS + DatasetConstants.PRODUCTION_INDEX];
	}
	
	public int getLength()
	{
		return timepoints.length;
	}

}
