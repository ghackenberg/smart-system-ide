package org.xtream.demo.thermal.model;

import org.xtream.core.model.Expression;

public class NetComponent extends EnergyComponent
{
	
	public NetComponent(int size)
	{
		terminals = new EnergyComponent[size + 2];
		
		terminals[0] = new SolarComponent(size * 400.);
		terminals[1] = new StorageComponent(size * 300., size * 4000.);
		
		for (int i = 0; i < size; i++)
		{
			terminals[i + 2] = new ThermalComponent();
		}
	}
	
	public NetComponent(EnergyComponent... terminals)
	{
		this.terminals = terminals;
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	/* none */
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public EnergyComponent[] terminals;
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////

	public Expression<Double> productionExpression = new Expression<Double>(productionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double production = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				production += terminal.productionOutput.get(timepoint);
			}
			
			return production;
		}
	};

	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double consumption = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				consumption += terminal.consumptionOutput.get(timepoint);
			}
			
			return consumption;
		}
	};
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double temperature = 0;
			int count = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				Double current = terminal.temperatureOutput.get(timepoint);
				
				if (current != null)
				{
					temperature += current;
					count++;
				}
			}
			
			return temperature / count;
		}
	};
	
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double level = 0;
			int count = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				Double current = terminal.levelOutput.get(timepoint);
				
				if (current != null)
				{
					level += current;
					count++;
				}
			}
			
			return level / count;
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	/* none */
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	/* none */
	
	////////////
	// CHARTS //
	////////////
	
	/* none */

}
