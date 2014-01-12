package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;

public class NetComponent extends EnergyComponent
{
	
	public NetComponent(int size)
	{
		terminals = new EnergyComponent[size + 2];
		
		terminals[0] = new SolarComponent(size * 400.);
		terminals[1] = new StorageComponent(size * 200., size * 4000.);
		
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

	public Expression<Double> productionExpression = new Expression<Double>(production)
	{
		@Override public Double evaluate(int timepoint)
		{
			double production = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				production += terminal.production.get(timepoint);
			}
			
			return production;
		}
	};

	public Expression<Double> consumptionExpression = new Expression<Double>(consumption)
	{
		@Override public Double evaluate(int timepoint)
		{
			double consumption = 0;
			
			for (EnergyComponent terminal : terminals)
			{
				consumption += terminal.consumption.get(timepoint);
			}
			
			return consumption;
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	public Chart energyChart = new Chart(production, consumption, balance);

}
