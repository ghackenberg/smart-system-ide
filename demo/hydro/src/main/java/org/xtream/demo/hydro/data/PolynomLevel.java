package org.xtream.demo.hydro.data;

public class PolynomLevel
{
	
	private int staustufe;
	
	private int level_past;
	private int level_order;
	
	private int inflow_past;
	private int inflow_order;
	
	private int outflow_past;
	private int outflow_order;
	
	private int maximum_past;
	
	private double[] beta;
	
	public PolynomLevel(int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		this.staustufe = staustufe;
		
		this.level_past = level_past;
		this.level_order = level_order;

		this.inflow_past = inflow_past;
		this.inflow_order = inflow_order;

		this.outflow_past = outflow_past;
		this.outflow_order = outflow_order;
		
		maximum_past = Math.max(level_past, Math.max(inflow_past, outflow_past));
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
	public int getOutflowPast()
	{
		return outflow_past;
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
		beta = PolynomTrainer.trainLevelModel(dataset, staustufe, level_past, level_order, inflow_past, inflow_order, outflow_past, outflow_order);
	}
	
	public double estimate(double[] levels, double[] inflows, double[] outflows)
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
		for (int j = 0; j < outflow_past; j++)
		{
			for (int k = 0; k < outflow_order; k++)
			{
				result += beta[1 + level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] * Math.pow(outflows[j], k + 1);
			}
		}
		
		return result;
	}

}
