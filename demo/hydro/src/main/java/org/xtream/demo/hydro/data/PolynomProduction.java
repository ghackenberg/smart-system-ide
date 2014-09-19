package org.xtream.demo.hydro.data;

public class PolynomProduction
{
	
	private double[] beta;
	
	private int staustufe;
	
	private int turbine_outflow_past;
	private int turbine_outflow_order;
	
	private int upper_level_past;
	private int upper_level_order;
	
	private int lower_level_past;
	private int lower_level_order;
	
	public PolynomProduction(int staustufe, int turbine_outflow_past, int turbine_outflow_order, int upper_level_past, int upper_level_order, int lower_level_past, int lower_level_order)
	{
		this.staustufe = staustufe;
		
		this.turbine_outflow_past = turbine_outflow_past;
		this.turbine_outflow_order = turbine_outflow_order;
		
		this.upper_level_past = upper_level_past;
		this.upper_level_order = upper_level_order;
		
		this.lower_level_past = lower_level_past;
		this.lower_level_order = lower_level_order;
	}
	
	public int getStaustufe()
	{
		return staustufe;
	}
	
	public int getTurbineOutflowPast()
	{
		return turbine_outflow_past;
	}
	public int getTurbineOutflowOrder()
	{
		return turbine_outflow_order;
	}
	
	public int getUpperLevelPast()
	{
		return upper_level_past;
	}
	public int getUpperLevelOrder()
	{
		return upper_level_order;
	}
	
	public int getLowerLevelPast()
	{
		return lower_level_past;
	}
	public int getLowerLevelOrder()
	{
		return lower_level_order;
	}
	
	public void fit(Dataset dataset)
	{
		beta = PolynomTrainer.trainProductionModel(dataset, staustufe, turbine_outflow_past, turbine_outflow_order, upper_level_past, upper_level_order, lower_level_past, lower_level_order);
	}
	
	public double estimate(double[] turbine_outflows, double[] upper_levels, double[] lower_levels)
	{
		double estimate = beta[0];

		for (int i = 0; i < turbine_outflow_past; i++)
		{
			for (int j = 0; j < turbine_outflow_order; j++)
			{
				estimate += beta[1 + i * turbine_outflow_order + j] * Math.pow(turbine_outflows[i], j + 1);
			}
		}
		for (int i = 0; i < upper_level_past; i++)
		{
			for (int j = 0; j < upper_level_order; j++)
			{
				estimate += beta[1 + turbine_outflow_past * turbine_outflow_order + i * upper_level_order + j] * Math.pow(upper_levels[i], j + 1);
			}
		}
		for (int i = 0; i < lower_level_past; i++)
		{
			for (int j = 0; j < lower_level_order; j++)
			{
				estimate += beta[1 + turbine_outflow_past * turbine_outflow_order + upper_level_past * upper_level_order + i * lower_level_order + j] * Math.pow(lower_levels[i], j + 1);
			}
		}
		
		return estimate;
	}

}
