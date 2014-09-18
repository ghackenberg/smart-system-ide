package org.xtream.demo.hydro.model.physics;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class Trainer
{
	
	public static double[] trainLevelModel(Dataset data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		System.out.println("trainRegressionModel(data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ")");
		
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
		
		/*
		for (int i = 0; i < data.length - MAXIMUM_PAST; i++)
		{
			System.out.print("Sample " + i + ": " + y[i] + " = ");
	
			for (int j = 0; j < x[i].length; j++)
			{
				System.out.print((j > 0 ? " + " : "") + "b_" + j + " * " + x[i][j]);
			}
			
			System.out.println();
		}
		*/
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		regression.newSampleData(y, x);
		
		double[] beta = regression.estimateRegressionParameters();
		
		/*
		for (int i = 0; i < beta.length; i++)
		{
			System.out.println("beta_" + i + " = " + beta[i]);
		}
		
		System.out.println(beta.length);
		System.out.println(LEVEL_PAST * LEVEL_ORDER + INFLOW_PAST * INFLOW_ORDER + OUTFLOW_PAST * OUTFLOW_ORDER);
		*/
		
		/*
		double[] residuals = regression.estimateResiduals();
		double[][] parametersVariance = regression.estimateRegressionParametersVariance();
		double regressandVariance = regression.estimateRegressandVariance();
		double rSquared = regression.calculateRSquared();
		double sigma = regression.estimateRegressionStandardError();
		*/
		
		return beta;
	}
	
	public static double[] trainProductionModel(Dataset dataset, int staustufe, int upper_level_past, int upper_level_order, int lower_level_past, int lower_level_order)
	{
		throw new IllegalStateException("Not implemented yet!");
	}

}
