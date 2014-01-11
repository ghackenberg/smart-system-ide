package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.OutputPort;
import org.xtream.core.model.annotations.Show;

public class NetComponent extends Component
{
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	@Show("Main")
	public OutputPort<Double> energy = new OutputPort<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public ThermalComponent thermal1 = new ThermalComponent();
	
	public ThermalComponent thermal2 = new ThermalComponent();
	
	public ThermalComponent thermal3 = new ThermalComponent();
	
	public SolarComponent solar = new SolarComponent();
	
	public StorageComponent storage = new StorageComponent();
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////

	public Expression<Double> energyExpression = new Expression<Double>(energy)
	{
		@Override public Double evaluate(int timepoint)
		{
			return thermal1.energy.get(timepoint) + thermal2.energy.get(timepoint) + thermal3.energy.get(timepoint) + solar.energy.get(timepoint) + storage.energy.get(timepoint);
		}
	};

}
