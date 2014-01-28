package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.model.expressions.ChannelExpression;

public abstract class RootComponent extends Component
{
	
	public static int DURATION = 96;
	public static int COVERAGE = 500;
	public static int CLASSES = 25;
	public static double RANDOMNESS = 0.25;
	
	public RootComponent(Stage stage)
	{
		this(stage, 10);
	}
	@SuppressWarnings("unchecked")
	public RootComponent(Stage stage, int size)
	{
		this.stage = stage;
		
		net = new NetComponent(size + 2);
		solar = stage.createSolar(size * 300.);
		storage = stage.createStorage(size * 200., size * 2000.);
		thermals = new ThermalComponent[size];
		
		for (int i = 0; i < size; i++)
		{
			thermals[i] = stage.createThermal();
		}
		
		balances = new ChannelExpression[size + 2];
		
		balances[0] = new ChannelExpression<>(net.balanceInputs[0], solar.balanceOutput);
		balances[1] = new ChannelExpression<>(net.balanceInputs[1], storage.balanceOutput);
		for (int i = 0; i < size; i++)
		{
			balances[i + 2] = new ChannelExpression<>(net.balanceInputs[i + 2], thermals[i].balanceOutput);
		}
		
		temperatureChart = new Chart(thermals[0].minimumOutput, temperatureOutput, thermals[0].maximumOutput);
		levelChart = new Chart(storage.minimumOutput, levelOutput, storage.maximumOutput);
	}
	
	// Parameters
	
	protected Stage stage;
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> levelOutput = new Port<>();
	
	// Components
	
	public NetComponent net;
	public SolarComponent solar;
	public StorageComponent storage;
	public ThermalComponent[] thermals;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	
	// Equivalences
	
	public Equivalence temperatureEquivalence = new Equivalence(temperatureOutput);
	public Equivalence levelEquivalence = new Equivalence(levelOutput);
	
	// Objectives
	
	public Objective costObjective = new Objective(costOutput, Direction.MIN);
	
	// Charts
	
	public Chart costChart = new Chart(costOutput);
	public Chart temperatureChart;
	public Chart levelChart;
	
	// Expressions
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			double sum = 0.;
			
			for (ThermalComponent thermal : thermals)
			{
				sum += thermal.temperatureOutput.get(timepoint) / thermals.length;
			}
			
			return sum;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override public Double evaluate(int timepoint)
		{
			return storage.levelOutput.get(timepoint);
		}
		
	};

}
