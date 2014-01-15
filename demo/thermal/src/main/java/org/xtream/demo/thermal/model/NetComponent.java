package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;

public class NetComponent extends EnergyComponent
{
	
	@SuppressWarnings("unchecked")
	public NetComponent(int size)
	{
		terminalInputs = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			terminalInputs[i] = new Port<>();
		}
	}
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double>[] terminalInputs;
	
	/////////////
	// OUTPUTS //
	/////////////
	
	/* none */
	
	////////////////
	// COMPONENTS //
	////////////////
	
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
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(timepoint);
				
				production += current > 0. ? current : 0.;
			}
			
			return production;
		}
	};
	public Expression<Double> consumptionExpression = new Expression<Double>(consumptionOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double consumption = 0;
			
			for (Port<Double> terminal : terminalInputs)
			{
				double current = terminal.get(timepoint);
				
				consumption += current < 0. ? current : 0.;
			}
			
			return consumption;
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
	
	//////////////
	// PREVIEWS //
	//////////////
	
	public Chart energyPreview = new Chart(productionOutput, consumptionOutput, balanceOutput);

}
