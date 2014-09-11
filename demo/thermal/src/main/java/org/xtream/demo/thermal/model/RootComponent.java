package org.xtream.demo.thermal.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.containers.Component;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantNonDeterministicExpression;
import org.xtream.core.model.markers.Equivalence;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.demo.thermal.model.stage.Stage;

public abstract class RootComponent extends Component
{
	
	public static int SIZE = 10;
	public static int DURATION = 96 * 3;
	public static int SAMPLES = 100;
	public static int CLASSES = 50;
	public static double RANDOMNESS = 0.0;
	public static double CACHING = 0.0;
	public static int ROUNDS = 50;
	
	public RootComponent(Stage stage)
	{
		this(stage, SIZE);
	}
	@SuppressWarnings("unchecked")
	public RootComponent(Stage stage, int size)
	{
		this.stage = stage;
		this.size = size;
		
		net = new NetComponent(size + 2);
		solar = stage.createSolar(size * 400.);
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
		
		probabilities = new ChannelExpression[size];
		for (int i = 0; i < size; i++)
		{
			probabilities[i] = new ChannelExpression<>(thermals[i].probabilityInput, probabilityOutput);
		}
		
		balanceChart = new Timeline(net.productionOutput, net.consumptionOutput, net.balanceOutput);
		temperatureChart = new Timeline(thermals[0].minimumOutput, temperatureOutput, thermals[0].maximumOutput);
		levelChart = new Timeline(storage.minimumOutput, levelOutput, storage.maximumOutput);
	}
	
	// Parameters
	
	protected Stage stage;
	protected int size;
	
	// Outputs
	
	public Port<Double> costOutput = new Port<>();
	public Port<Double> temperatureOutput = new Port<>();
	public Port<Double> levelOutput = new Port<>();
	public Port<Double> probabilityOutput = new Port<>();
	
	// Components
	
	public NetComponent net;
	public SolarComponent solar;
	public StorageComponent storage;
	public ThermalComponent[] thermals;
	
	// Channels
	
	public ChannelExpression<Double>[] balances;
	public ChannelExpression<Double>[] probabilities;
	
	// Equivalences
	
	public Equivalence temperatureEquivalence = new Equivalence(temperatureOutput);
	public Equivalence levelEquivalence = new Equivalence(levelOutput);
	
	// Objectives
	
	public Objective costObjective = new MinObjective(costOutput);
	
	// Charts
	
	public Chart costChart = new Timeline(costOutput);
	public Chart balanceChart;
	public Chart temperatureChart;
	public Chart levelChart;
	
	// Expressions
	
	public Expression<Double> temperatureExpression = new Expression<Double>(temperatureOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0.;
			
			for (ThermalComponent thermal : thermals)
			{
				sum += thermal.temperatureOutput.get(state, timepoint) / thermals.length;
			}
			
			return sum;
		}
	};
	public Expression<Double> levelExpression = new Expression<Double>(levelOutput)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return storage.levelOutput.get(state, timepoint);
		}
	};
	public Expression<Double> probabilityExpression = new ConstantNonDeterministicExpression<>(probabilityOutput, 0., .25, .5, .75, 1.);

}
