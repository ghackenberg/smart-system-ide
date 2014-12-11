package org.xtream.demo.hydro.data;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class PolynomTrainer
{
	
	public static double[] trainLevelDirectModel(Dataset data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		System.out.println("trainLevelModel(data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ")");
		
		int maximum_past = Math.max(level_past + 1, Math.max(inflow_past, outflow_past));
		
		double[] y = new double[data.getLength() - maximum_past];
		double[][] x = new double[data.getLength() - maximum_past][level_past * level_order + inflow_past * inflow_order + outflow_past * outflow_order];
		
		for (int i = maximum_past; i < data.getLength(); i++)
		{
			y[i - maximum_past] = data.getLevel(staustufe, i);
			
			for (int j = 0; j < level_past; j++)
			{
				for (int k = 0; k < level_order; k++)
				{
					x[i - maximum_past][j * level_order + k] = Math.pow(data.getLevel(staustufe, i - level_past + j), k + 1);
				}
			}
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + j * inflow_order + k] = Math.pow(data.getInflow(staustufe, i - inflow_past + j + 1), k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] = Math.pow(data.getOutflowTotal(staustufe, i - outflow_past + j + 1), k + 1);
				}
			}
		}
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		double[] beta = regression.estimateRegressionParameters();
		
		for (int i = 0; i < beta.length; i++)
		{
			System.out.println("beta_" + i + " = " + beta[i]);
		}
		
		return beta;
	}
	
	public static double[] trainLevelDeltaModel(Dataset data, int staustufe, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		System.out.println("trainLevelDeltaModel(data, " + staustufe + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ")");
		
		int maximum_past = Math.max(inflow_past, outflow_past);
		
		double[] y = new double[data.getLength() - maximum_past];
		double[][] x = new double[data.getLength() - maximum_past][inflow_past * inflow_order + outflow_past * outflow_order];
		
		for (int i = maximum_past; i < data.getLength(); i++)
		{
			y[i - maximum_past] = data.getLevel(staustufe, i) - data.getLevel(staustufe, i - 1);
			
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					x[i - maximum_past][j * inflow_order + k] = Math.pow(data.getInflow(staustufe, i - inflow_past + j + 1), k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					x[i - maximum_past][inflow_past * inflow_order + j * outflow_order + k] = Math.pow(data.getOutflowTotal(staustufe, i - outflow_past + j + 1), k + 1);
				}
			}
		}
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		double[] beta = regression.estimateRegressionParameters();
		
		for (int i = 0; i < beta.length; i++)
		{
			System.out.println("beta_" + i + " = " + beta[i]);
		}
		
		return beta;
	}
	
	public static double[] trainLevelLastModel(Dataset data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order)
	{
		System.out.println("trainLevelLastModel(data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ")");
		
		int maximum_past = Math.max(level_past + 1, inflow_past);
		
		double[] y = new double[data.getLength() - maximum_past];
		double[][] x = new double[data.getLength() - maximum_past][level_past * level_order + inflow_past * inflow_order];
		
		for (int i = maximum_past; i < data.getLength(); i++)
		{
			y[i - maximum_past] = data.getLevel(staustufe, i);
			
			for (int j = 0; j < level_past; j++)
			{
				for (int k = 0; k < level_order; k++)
				{
					x[i - maximum_past][j * level_order + k] = Math.pow(data.getLevel(staustufe, i - level_past + j), k + 1);
				}
			}
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + j * inflow_order + k] = Math.pow(data.getInflow(staustufe, i - inflow_past + j + 1), k + 1);
				}
			}
		}
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		return regression.estimateRegressionParameters();
	}
	
	public static double[] trainProductionModel(Dataset dataset, int staustufe, int turbine_outflow_past, int turbine_outflow_order, int upper_level_past, int upper_level_order, int lower_level_past, int lower_level_order)
	{
		System.out.println("trainProductionModel(dataset, " + staustufe + ", " + turbine_outflow_past + ", " + turbine_outflow_order + ", " + upper_level_past + ", " + upper_level_order + ", " + lower_level_past + ", " + lower_level_order + ")");
		
		int maximum_past = Math.max(turbine_outflow_past, Math.max(upper_level_past, lower_level_past));
		
		double[] y = new double[dataset.getLength() - maximum_past];
		double[][] x = new double[dataset.getLength() - maximum_past][turbine_outflow_past * turbine_outflow_order + upper_level_past * upper_level_order + lower_level_past * lower_level_order];
		
		for (int i = maximum_past; i < dataset.getLength(); i++)
		{
			y[i - maximum_past] = dataset.getProduction(staustufe, i);

			for (int j = 0; j < turbine_outflow_past; j++)
			{
				for (int k = 0; k < turbine_outflow_order; k++)
				{
					x[i - maximum_past][j * turbine_outflow_order + k] = Math.pow(dataset.getOutflowTurbine(staustufe, i - turbine_outflow_past + j + 1), k + 1); 
				}
			}
			for (int j = 0; j < upper_level_past; j++)
			{
				for (int k = 0; k < upper_level_order; k++)
				{
					x[i - maximum_past][turbine_outflow_past * turbine_outflow_order + j * upper_level_order + k] = Math.pow(dataset.getLevel(staustufe, i - upper_level_past + j + 1), k + 1); 
				}
			}
			for (int j = 0; j < lower_level_past; j++)
			{
				for (int k = 0; k < lower_level_order; k++)
				{
					x[i - maximum_past][turbine_outflow_past * turbine_outflow_order + upper_level_past * upper_level_order + j * lower_level_order + k] = Math.pow(dataset.getLevel(staustufe + 1, i - lower_level_past + j + 1), k + 1); 
				}
			}
		}
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		return regression.estimateRegressionParameters();
	}

}
