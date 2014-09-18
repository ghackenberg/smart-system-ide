package org.xtream.demo.hydro.model.physics;

public class PolynomProduction
{
	
	private double[] beta;
	
	private int staustufe;
	
	private int upper_level_past;
	private int upper_level_order;
	
	private int lower_level_past;
	private int lower_level_order;
	
	public PolynomProduction(int staustufe, int upper_level_past, int upper_level_order, int lower_level_past, int lower_level_order)
	{
		this.staustufe = staustufe;
		
		this.upper_level_past = upper_level_past;
		this.upper_level_order = upper_level_order;
		
		this.lower_level_past = lower_level_past;
		this.lower_level_order = lower_level_order;
	}
	
	public void fit(Dataset dataset)
	{
		beta = Trainer.trainProductionModel(dataset, staustufe, upper_level_past, upper_level_order, lower_level_past, lower_level_order);
	}
	
	public double estimate(double[] upper_levels, double[] lower_levels)
	{
		double estimate = beta[0];
		
		for (int i = 0; i < upper_level_past; i++)
		{
			for (int j = 0; j < upper_level_order; j++)
			{
				estimate += beta[1 + i * upper_level_order + j] * Math.pow(upper_levels[i], j + 1);
			}
		}
		for (int i = 0; i < lower_level_past; i++)
		{
			for (int j = 0; j < lower_level_order; j++)
			{
				estimate += beta[1 + upper_level_past * upper_level_order + i * lower_level_order + j] * Math.pow(lower_levels[i], j + 1);
			}
		}
		
		return estimate;
	}

}
