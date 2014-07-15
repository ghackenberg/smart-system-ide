package org.xtream.demo.hydro.model.actual;

import java.io.FileReader;
import java.util.List;
import java.util.Vector;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;

import au.com.bytecode.opencsv.CSVReader;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Constructors
	
	public ControlComponent(String weekFile)
	{
		try
		{
			CSVReader reader = new CSVReader(new FileReader(weekFile), ';');
			
			List<String[]> rows = reader.readAll();
			
			for (int rowIndex = 2; rowIndex < rows.size(); rowIndex++)
			{
				String[] row = rows.get(rowIndex);
				
				Double[] temp = new Double[row.length];
				
				for (int columnIndex = 0; columnIndex < row.length; columnIndex++)
				{
					try
					{
						temp[columnIndex] = Double.parseDouble(row[columnIndex].replace(',', '.'));
					}
					catch (Exception e)
					{
						// System.out.println("Exception occured at " + rowIndex + "/" + columnIndex + " = " + row[columnIndex]);
					}
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
			return values.get(timepoint + 2)[7];
		}
	};
	public Expression<Double> hauptkraftwerkWeirDischarge = new Expression<Double>(hauptkraftwerkWeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[8];
		}
	};
	public Expression<Double> wehr1TurbineDischarge = new Expression<Double>(wehr1TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[13];
		}
	};
	public Expression<Double> wehr1WeirDischarge = new Expression<Double>(wehr1WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[14];
		}
	};
	public Expression<Double> wehr2TurbineDischarge = new Expression<Double>(wehr2TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[19];
		}
	};
	public Expression<Double> wehr2WeirDischarge = new Expression<Double>(wehr2WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[20];
		}
	};
	public Expression<Double> wehr3TurbineDischarge = new Expression<Double>(wehr3TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[25];
		}
	};
	public Expression<Double> wehr3WeirDischarge = new Expression<Double>(wehr3WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[26];
		}
	};
	public Expression<Double> wehr4TurbineDischarge = new Expression<Double>(wehr4TurbineDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[30];
		}
	};
	public Expression<Double> wehr4WeirDischarge = new Expression<Double>(wehr4WeirDischargeOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return values.get(timepoint + 2)[33];
		}
	};
	
}
