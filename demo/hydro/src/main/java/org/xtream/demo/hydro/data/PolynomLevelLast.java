package org.xtream.demo.hydro.data;

public class PolynomLevelLast
{
	
	private int staustufe;
	
	private int level_past;
	private int level_order;
	
	private int inflow_past;
	private int inflow_order;
	
	private int maximum_past;
	
	private double[] beta;
	
	public PolynomLevelLast(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order)
	{
		this.staustufe = staustufe;
		
		this.level_past = level_past;
		this.level_order = level_order;

		this.inflow_past = inflow_past;
		this.inflow_order = inflow_order;
		
		maximum_past = Math.max(level_past, inflow_past);
	}
	
	public int getStaustufe()
	{
		return staustufe;
	}
	public int getLevelPast()
	{
		return level_past;
	}
	public int getInflowPast()
	{
		return inflow_past;
	}
	public int getMaximumPast()
	{
		return maximum_past;
	}
	
	public double[] getBeta()
	{
		return beta;
	}
	
	public void fit(Dataset dataset)
	{
		beta = PolynomTrainer.trainLevelLastModel(dataset, staustufe, level_past, level_order, inflow_past, inflow_order);
	}
	
	public double estimate(double[] levels, double[] inflows)
	{
		double result = beta[0];
		
		for (int j = 0; j < level_past; j++)
		{
			for (int k = 0; k < level_order; k++)
			{
				result += beta[1 + j * level_order + k] * Math.pow(levels[j], k + 1);
			}
		}
		for (int j = 0; j < inflow_past; j++)
		{
			for (int k = 0; k < inflow_order; k++)
			{
				result += beta[1 + level_past * level_order + j * inflow_order + k] * Math.pow(inflows[j], k + 1);
			}
		}
		
		return result;
	}

}
