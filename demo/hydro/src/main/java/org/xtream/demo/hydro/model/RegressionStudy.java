package org.xtream.demo.hydro.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import au.com.bytecode.opencsv.CSVReader;

public class RegressionStudy
{
	
	public static final int INFLOW_PAST = 20;
	public static final int LEVEL_PAST = 20;
	public static final int OUTFLOW_PAST = 20;
	public static final int MAXIMUM_PAST = Math.max(LEVEL_PAST + 1, Math.max(INFLOW_PAST, OUTFLOW_PAST));

	public static void main(String[] args)
	{
		try
		{
			System.out.println("Reading data ...");
			
			CSVReader reader_2011 = new CSVReader(new FileReader("csv/Regression_2011.csv"), ';');
			CSVReader reader_2012 = new CSVReader(new FileReader("csv/Regression_2012.csv"), ';');
			
			List<String[]> lines_2011 = reader_2011.readAll();
			List<String[]> lines_2012 = reader_2012.readAll();
			
			/*
			for (int i = 0; i < lines_2011.size(); i++)
			{
				System.out.print("Row " + i + ": ");
				
				for (int j = 0; j < lines_2011.get(i).length; j++)
				{
					System.out.print((j > 0 ? ";" : "") + lines_2011.get(i)[j]);
				}
				
				System.out.println();
			}
			*/
			
			double[][] data_2011 = new double[lines_2011.size() - 1][15];
			double[][] data_2012 = new double[lines_2012.size() - 1][15];
			
			for (int i = 1; i < lines_2011.size(); i++)
			{
				for (int j = 1; j < 16; j++)
				{
					data_2011[i - 1][j - 1] = Double.parseDouble(lines_2011.get(i)[j].replace(',', '.'));
				}
			}
			for (int i = 1; i < lines_2012.size(); i++)
			{
				for (int j = 1; j < 16; j++)
				{
					data_2012[i - 1][j - 1] = Double.parseDouble(lines_2012.get(i)[j].replace(',', '.'));
				}
			}
			
			reader_2011.close();
			reader_2012.close();
			
			/*
			for (int i = 0; i < data_2011.length; i++)
			{
				System.out.print("Row " + i + ": ");
				
				for (int j = 0; j < data_2011[i].length; j++)
				{
					System.out.print((j > 0 ? "," : "") + data_2011[i][j]);
				}
				
				System.out.println();
			}
			*/
			
			System.out.println("Fitting model ...");
			
			OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
			
			double[] y = new double[data_2011.length - MAXIMUM_PAST];
			double[][] x = new double[data_2011.length - MAXIMUM_PAST][LEVEL_PAST + INFLOW_PAST + OUTFLOW_PAST];
			
			for (int i = 0; i < data_2011.length - MAXIMUM_PAST; i++)
			{
				y[i] = data_2011[i + MAXIMUM_PAST][1];
				
				for (int j = 0; j < LEVEL_PAST; j++)
				{
					x[i][j] = data_2011[i + MAXIMUM_PAST - 1 - j][1];
				}
				for (int j = 0; j < INFLOW_PAST; j++)
				{
					x[i][LEVEL_PAST + j] = data_2011[i + MAXIMUM_PAST - j][0];
				}
				for (int j = 0; j < OUTFLOW_PAST; j++)
				{
					x[i][LEVEL_PAST + INFLOW_PAST + j] = data_2011[i + MAXIMUM_PAST - j][2];
				}
			}
			
			/*
			for (int i = 0; i < data_2011.length - MAXIMUM_PAST; i++)
			{
				System.out.print("Sample " + i + ": " + y[i] + " = ");

				for (int j = 0; j < x[i].length; j++)
				{
					System.out.print((j > 0 ? " + " : "") + "b_" + j + " * " + x[i][j]);
				}
				
				System.out.println();
			}
			*/
			
			regression.newSampleData(y, x);
			
			double[] beta = regression.estimateRegressionParameters();
			
			for (int i = 0; i < beta.length; i++)
			{
				System.out.println("beta_" + i + " = " + beta[i]);
			}
			
			/*
			double[] residuals = regression.estimateResiduals();
			double[][] parametersVariance = regression.estimateRegressionParametersVariance();
			double regressandVariance = regression.estimateRegressandVariance();
			double rSquared = regression.calculateRSquared();
			double sigma = regression.estimateRegressionStandardError();
			*/
			
			System.out.println("Testing model ...");
			
			FileWriter result = new FileWriter("Regression.csv");
			
			result.write("Measured;Estimated\n");
			
			for (int i = MAXIMUM_PAST; i < data_2012.length; i++)
			{
				double y_measured = data_2012[i][1];
				double y_estimated = 0;

				for (int j = 0; j < LEVEL_PAST; j++)
				{
					y_estimated += beta[j] * data_2012[i - 1 - j][1];
				}
				for (int j = 0; j < INFLOW_PAST; j++)
				{
					y_estimated += beta[LEVEL_PAST + j] * data_2012[i - j][0];
				}
				for (int j = 0; j < OUTFLOW_PAST; j++)
				{
					y_estimated += beta[LEVEL_PAST + INFLOW_PAST + j] * data_2012[i - j][2];
				}
				
				result.write(String.valueOf(y_measured).replace('.',',') + ";" + String.valueOf(y_estimated).replace('.',',') + "\n");
			}
			
			result.close();
			
			System.out.println("Finish study ...");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
