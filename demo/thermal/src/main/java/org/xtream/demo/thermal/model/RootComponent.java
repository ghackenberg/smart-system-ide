package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Workbench<>(RootComponent.class, 96, 500, 50, 0.);
	}
	
	public RootComponent()
	{
		this(100);
	}
	
	@SuppressWarnings("unchecked")
	public RootComponent(int size)
	{
		// Thermals
		
		thermals = new ThermalComponent[size];
		
		for (int i = 0; i < thermals.length; i++)
		{
			thermals[i] = new ThermalComponent();
		}
		
		// Solar
		
		solar = new SolarComponent(size * 400.);
		
		// Storage
		
		storage = new StorageComponent(size * 200., size * 4000.);
		
		// Net
		
		net = new NetComponent(size + 2);
		
		// Level
		
		level = new ChannelExpression<>(levelOutput, storage.levelOutput);
		
		// Balance
		
		balances = new ChannelExpression[size + 2];
		
		for (int i = 0; i < thermals.length; i++)
		{
			balances[i] = new ChannelExpression<>(net.terminalInputs[i], thermals[i].balanceOutput);
		}
		
		balances[size + 0] = new ChannelExpression<>(net.terminalInputs[size + 0], storage.balanceOutput);
		balances[size + 1] = new ChannelExpression<>(net.terminalInputs[size + 1], solar.balanceOutput);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> levelOutput = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public NetComponent net;
	public SolarComponent solar;
	public StorageComponent storage;
	public ThermalComponent[] thermals;
	
	//////////////
	// CHANNELS //
	//////////////
	
	public ChannelExpression<Double>[] balances;
	public ChannelExpression<Double> storageBalance;
	public ChannelExpression<Double> solarBalance;
	public ChannelExpression<Double> level;
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> costExpression = new Expression<Double>(costOutput)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += net.balanceOutput.get(timepoint) * net.balanceOutput.get(timepoint);
		}
	};
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double average = 0.;
			
			for (ThermalComponent thermal : thermals)
			{
				average += thermal.temperatureOutput.get(timepoint) / thermals.length;
			}
			
			return average;
		}
	};
	
	/////////////////
	// CONSTRAINTS //
	/////////////////
	
	/* none */
	
	//////////////////
	// EQUIVALENCES //
	//////////////////
	
	public Equivalence temperatureEquivalence = new Equivalence(temperatureOutput);
	public Equivalence levelEquivalence = new Equivalence(levelOutput);
	
	/////////////////
	// PREFERENCES //
	/////////////////
	
	/* none */
	
	////////////////
	// OBJECTIVES //
	////////////////
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	////////////
	// CHARTS //
	////////////
	
	public Chart costChart = new Chart(costOutput);
	public Chart temperatureChart = new Chart(temperatureOutput);
	public Chart levelChart = new Chart(levelOutput);
	
	//////////////
	// PREVIEWS //
	//////////////
	
	public Chart costPreview = new Chart(costOutput);

}
