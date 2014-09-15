package org.xtream.demo.hydro.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import au.com.bytecode.opencsv.CSVReader;

public class RegressionStudy
{
	
	public static final int DAY = 4 * 24;
	public static final int WEEK = DAY * 7;
	public static final int MONTH = WEEK * 4;
	
	public static final int STAUSTUFE = 0;
	
	public static final int INFLOW_PAST = 10;
	public static final int INFLOW_ORDER = 5;
	
	public static final int LEVEL_PAST = 1;
	public static final int LEVEL_ORDER = 1;
	
	public static final int OUTFLOW_PAST = 10;
	public static final int OUTFLOW_ORDER = 5;
	
	public static final int MAXIMUM_PAST = Math.max(LEVEL_PAST + 1, Math.max(INFLOW_PAST, OUTFLOW_PAST));

	public static void main(String[] args)
	{
		try
		{	
			double[][] data_2011 = parseData("csv/Regression_2011.csv");
			double[][] data_2012 = parseData("csv/Regression_2012.csv");
			
			double[] beta = trainModel(data_2012);
			
			FileWriter overview = new FileWriter("csv/Comparison/Overview.csv");
			
			overview.write("Week;Year 2011 (Average);Year 2011 (Quadratic);Year 2011 (Maximum);Year 2012 (Average);Year 2012 (Quadratic);Year 2012 (Maximum)\n");
			
			for (int i = 1; i < 35; i++)
			{
				double[] error_2011 = testModel(beta, data_2011, WEEK * i, WEEK * 1, "csv/Comparison/2012_vs_2011/Week_" + i + ".csv");
				double[] error_2012 = testModel(beta, data_2012, WEEK * i, WEEK * 1, "csv/Comparison/2012_vs_2012/Week_" + i + ".csv");
				
				overview.write(i + 1 + ";");
				overview.write(String.valueOf(error_2011[0]).replace('.',',') + ";");
				overview.write(String.valueOf(error_2011[1]).replace('.',',') + ";");
				overview.write(String.valueOf(error_2011[2]).replace('.',',') + ";");
				overview.write(String.valueOf(error_2012[0]).replace('.',',') + ";");
				overview.write(String.valueOf(error_2012[1]).replace('.',',') + ";");
				overview.write(String.valueOf(error_2012[2]).replace('.',',') + ";");
				overview.write("\n");
			}
			
			overview.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static double[][] parseData(String file) throws IOException
	{
		System.out.println("parseData(\"" + file + "\")");
		
		CSVReader reader = new CSVReader(new FileReader(file), ';');
		
		List<String[]> lines_string = reader.readAll();
		List<double[]> lines_double = new ArrayList<>();
		
		for (int i = 1; i < lines_string.size(); i++)
		{
			// Check if we have valid level measurements
			
			boolean valid = true;
			
			for (int j = 2; j < 16; j+=3)
			{
				double level = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
				
				valid = valid && level > 0;
			}
			
			// Add the measurements or stop parsing
			
			if (valid)
			{
				lines_double.add(new double[15]);
				
				for (int j = 1; j < 16; j++)
				{
					lines_double.get(i - 1)[j - 1] = Double.parseDouble(lines_string.get(i)[j].replace(',', '.'));
				}
			}
			else
			{
				break;
			}
		}
		
		reader.close();
		
		System.out.println(lines_string.size() + " vs " + lines_double.size());
		
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
		
		return lines_double.toArray(new double[lines_double.size()][15]);
	}
	
	private static double[] trainModel(double[][] data)
	{
		System.out.println("trainModel(data)");
		
		double[] y = new double[data.length - MAXIMUM_PAST];
		double[][] x = new double[data.length - MAXIMUM_PAST][LEVEL_PAST * LEVEL_ORDER + INFLOW_PAST * INFLOW_ORDER + OUTFLOW_PAST * OUTFLOW_ORDER];
		
		for (int i = MAXIMUM_PAST; i < data.length; i++)
		{
			y[i - MAXIMUM_PAST] = data[i][STAUSTUFE * 3 + 1];
			
			for (int j = 0; j < LEVEL_PAST; j++)
			{
				for (int k = 0; k < LEVEL_ORDER; k++)
				{
					x[i - MAXIMUM_PAST][j * LEVEL_ORDER + k] = Math.pow(data[i - 1 - j][STAUSTUFE * 3 + 1], k + 1);
				}
			}
			for (int j = 0; j < INFLOW_PAST; j++)
			{
				for (int k = 0; k < INFLOW_ORDER; k++)
				{
					x[i - MAXIMUM_PAST][LEVEL_PAST * LEVEL_ORDER + j * INFLOW_ORDER + k] = Math.pow(data[i - j][STAUSTUFE * 3 + 0], k + 1);
				}
			}
			for (int j = 0; j < OUTFLOW_PAST; j++)
			{
				for (int k = 0; k < OUTFLOW_ORDER; k++)
				{
					x[i - MAXIMUM_PAST][LEVEL_PAST * LEVEL_ORDER + INFLOW_PAST * INFLOW_ORDER + j * OUTFLOW_ORDER + k] = Math.pow(data[i - j][STAUSTUFE * 3 + 2], k + 1);
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
	
	private static double[] testModel(double[] beta, double[][] data, int start, int length, String file) throws IOException
	{
		System.out.println("testModel(beta, data, \"" + file + "\")");
	
		FileWriter result = new FileWriter(file);
		
		result.write("Measured;Estimated;Quotient\n");
		
		int count = 0;
		double error_quadratic = 0;
		double error_max = 0;
		double error_average = 0;
		double[] y_estimated = new double[length + MAXIMUM_PAST];
		
		// Initialize estimated levels
		
		for (int i = 0; i < length + MAXIMUM_PAST; i++)
		{
			y_estimated[i] = data[start - MAXIMUM_PAST + i][STAUSTUFE * 3 + 1];
		}
		
		// Calculate estimated levels
		
		for (int i = MAXIMUM_PAST; i < MAXIMUM_PAST + length; i++)
		{	
			double y_measured = data[start - MAXIMUM_PAST + i][STAUSTUFE * 3 + 1];
			
			// Estimate level
			
			y_estimated[i] = beta[0];

			for (int j = 0; j < LEVEL_PAST; j++)
			{
				for (int k = 0; k < LEVEL_ORDER; k++)
				{
					y_estimated[i] += beta[1 + j * LEVEL_ORDER + k] * Math.pow(y_estimated[i - 1 - j], k + 1);
				}
			}
			for (int j = 0; j < INFLOW_PAST; j++)
			{
				for (int k = 0; k < INFLOW_ORDER; k++)
				{
					y_estimated[i] += beta[1 + LEVEL_PAST * LEVEL_ORDER + j * INFLOW_ORDER + k] * Math.pow(data[start - MAXIMUM_PAST + i - j][STAUSTUFE * 3 + 0], k + 1);
				}
			}
			for (int j = 0; j < OUTFLOW_PAST; j++)
			{
				for (int k = 0; k < OUTFLOW_ORDER; k++)
				{
					y_estimated[i] += beta[1 + LEVEL_PAST * LEVEL_ORDER + INFLOW_PAST * INFLOW_ORDER + j * OUTFLOW_ORDER + k] * Math.pow(data[start - MAXIMUM_PAST + i - j][STAUSTUFE * 3 + 2], k + 1);
				}
			}
			
			if (y_estimated[i] == Double.NaN)
			{
				System.out.println("Level estimated not a number: " + i);
				
				break;
			}
			
			result.write(String.valueOf(y_measured).replace('.',',') + ";" + String.valueOf(y_estimated[i]).replace('.',',') + ";" + String.valueOf(y_estimated[i] / y_measured).replace('.',',') + "\n");
			
			count++;

			error_quadratic += (y_measured - y_estimated[i]) * (y_measured - y_estimated[i]);
			error_max = Math.max(error_max, Math.abs(y_measured - y_estimated[i]));
			error_average += Math.abs(y_measured - y_estimated[i]);
		}
		
		error_quadratic /= count;
		error_average /= count;
		
		//System.out.println("Quadratic error: " + error);
		
		result.close();
		
		return new double[] {error_average, error_quadratic, error_max};
	}

}
