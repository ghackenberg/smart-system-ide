package org.xtream.demo.hydro.model.physics;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.Perceptron;

public class Trainer
{
	
	public static double[] trainRegressionModel(double[][] data, int staustufe, int level_past, int level_order, int inflow_past, int inflow_order, int outflow_past, int outflow_order)
	{
		System.out.println("trainRegressionModel(data, " + staustufe + ", " + level_past + ", " + level_order + ", " + inflow_past + ", " + inflow_order + ", " + outflow_past + ", " + outflow_order + ")");
		
		int maximum_past = Math.max(level_past + 1, Math.max(inflow_past, outflow_past));
		
		double[] y = new double[data.length - maximum_past];
		double[][] x = new double[data.length - maximum_past][level_past * level_order + inflow_past * inflow_order + outflow_past * outflow_order];
		
		for (int i = maximum_past; i < data.length; i++)
		{
			y[i - maximum_past] = data[i][staustufe * 3 + 1];
			
			for (int j = 0; j < level_past; j++)
			{
				for (int k = 0; k < level_order; k++)
				{
					x[i - maximum_past][j * level_order + k] = Math.pow(data[i - 1 - j][staustufe * 3 + 1], k + 1);
				}
			}
			for (int j = 0; j < inflow_past; j++)
			{
				for (int k = 0; k < inflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + j * inflow_order + k] = Math.pow(data[i - j][staustufe * 3 + 0], k + 1);
				}
			}
			for (int j = 0; j < outflow_past; j++)
			{
				for (int k = 0; k < outflow_order; k++)
				{
					x[i - maximum_past][level_past * level_order + inflow_past * inflow_order + j * outflow_order + k] = Math.pow(data[i - j][staustufe * 3 + 2], k + 1);
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
	
	public static NeuralNetwork<?> trainNeuralModel(double[][] data, int staustufe, int level_past, int inflow_past, int outflow_past)
	{
		System.out.println("trainNeuralModel(data, " + staustufe + ", " + level_past + ", " + inflow_past + ", " + outflow_past + ")");
		
		int maximum_past = Math.max(level_past, Math.max(inflow_past, outflow_past));
		
		DataSet training = new DataSet(level_past + inflow_past + outflow_past, 1);
		
		for (int i = maximum_past; i < data.length / 1000; i++)
		{
			double[] input = new double[level_past + inflow_past + outflow_past];
			double[] output = new double[1];
			
			for (int j = 0; j < level_past;  j++)
			{
				input[j] = data[i - 1 - j][1];
			}
			for (int j = 0; j < inflow_past;  j++)
			{
				input[level_past + j] = data[i - j][0];
			}
			for (int j = 0; j < outflow_past;  j++)
			{
				input[level_past + inflow_past + j] = data[i - j][2];
			}
			output[0] = data[i][1];
			
			training.addRow(input, output);
		}
		
		NeuralNetwork<?> network = new Perceptron(level_past + inflow_past + outflow_past, 1);
		network.learn(training);
		
		return network;
	}

}
