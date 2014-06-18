package org.xtream.demo.hydro.model.actual;

import java.io.FileReader;
import java.util.Vector;

import org.xtream.core.model.Expression;
import org.xtream.core.optimizer.State;

import au.com.bytecode.opencsv.CSVReader;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	public static String WEEK_1 = "csv/All_week_2_2011.csv";
	public static String WEEK_2 = "csv/All_week_6_2011.csv";
	public static String WEEK_3 = "csv/All_week_14_2011.csv";
	public static String WEEK_4 = "csv/All_week_24_2011.csv";
	
	// Constructors
	
	public ControlComponent(String weekFile)
	{
		try
		{
			CSVReader reader = new CSVReader(new FileReader(weekFile), ';');
			
			for (String[] line : reader.readAll())
			{
				Double[] temp = new Double[line.length];
				
				for (int i = 0; i < line.length; i++)
				{
					temp[i] = Double.parseDouble(line[i].replace(',', '.'));
				}
				
				values.add(temp);
			}
			
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Parameters
	
	protected Vector<Double[]> values = new Vector<>();
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischarge = new Expression<Double>(hauptkraftwerkTurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[5] + values.get(timepoint + 2)[6];
		}
	};
	public Expression<Double> hauptkraftwerkWeirDischarge = new Expression<Double>(hauptkraftwerkWeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[7];
		}
	};
	public Expression<Double> wehr1TurbineDischarge = new Expression<Double>(wehr1TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[9];
		}
	};
	public Expression<Double> wehr1WeirDischarge = new Expression<Double>(wehr1WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
	};
	public Expression<Double> wehr2TurbineDischarge = new Expression<Double>(wehr2TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[13];
		}
	};
	public Expression<Double> wehr2WeirDischarge = new Expression<Double>(wehr2WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
	};
	public Expression<Double> wehr3TurbineDischarge = new Expression<Double>(wehr3TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[17];
		}
	};
	public Expression<Double> wehr3WeirDischarge = new Expression<Double>(wehr3WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
	};
	public Expression<Double> wehr4TurbineDischarge = new Expression<Double>(wehr4TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[23];
		}
	};
	public Expression<Double> wehr4WeirDischarge = new Expression<Double>(wehr4WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[24] + values.get(timepoint + 2)[25];
		}
	};
	
}
