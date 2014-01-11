package org.xtream.demo.thermal.model;

import org.xtream.core.model.Expression;

public class NetComponent extends EnergyComponent
{
	
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
	
	public EnergyComponent thermal1 = new ThermalComponent();
	
	public EnergyComponent thermal2 = new ThermalComponent();
	
	public EnergyComponent thermal3 = new ThermalComponent();
	
	public EnergyComponent solar = new SolarComponent();
	
	public EnergyComponent storage = new StorageComponent();
	
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
			return thermal1.production.get(timepoint) + thermal2.production.get(timepoint) + thermal3.production.get(timepoint) + solar.production.get(timepoint) + storage.production.get(timepoint);
		}
	};

	public Expression<Double> consumptionExpression = new Expression<Double>(consumption)
	{
		@Override public Double evaluate(int timepoint)
		{
			return thermal1.consumption.get(timepoint) + thermal2.consumption.get(timepoint) + thermal3.consumption.get(timepoint) + solar.consumption.get(timepoint) + storage.consumption.get(timepoint);
		}
	};

}
