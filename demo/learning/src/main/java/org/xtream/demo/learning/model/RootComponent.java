package org.xtream.demo.learning.model;

import org.xtream.core.model.Chart;
import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.charts.Timeline;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.optimizer.beam.Engine;
import org.xtream.core.workbench.Workbench;

public class RootComponent extends Component
{
	
	public static final int SIZE = 10;
	public static final int DURATION = 100;
	public static final int SAMPLES = 50;
	public static final int CLUSTERS = 100;
	public static final int BRANCH_ROUNDS = 1;
	public static final int BRANCH_DURATION = 5;
	
	public static void main(String[] args)
	{
		new Workbench<>(new Engine<Component>(new RootComponent(SIZE), SAMPLES, CLUSTERS, BRANCH_ROUNDS, BRANCH_DURATION), DURATION);
	}
	
	// Constructors
	
	@SuppressWarnings("unchecked")
	public RootComponent(int size)
	{
		contexts = new ContextComponent[size];
		
		for (int i = 0; i < size; i++)
		{
			contexts[i] = new ContextComponent();
		}
		
		controls = new ControlComponent[size];
		
		for (int i = 0; i < size; i++)
		{
			controls[i] = new ControlComponent();
		}
		
		constraints = new ConstraintComponent[size];
		
		for (int i = 0; i < size; i++)
		{
			constraints[i] = new ConstraintComponent();
		}
		
		objective = new ObjectiveComponent(size);
		
		equivalence = new EquivalenceComponent(size);
		
		decisions = new ChannelExpression[size];
		
		for (int i = 0; i < size; i++)
		{
			decisions[i] = new ChannelExpression<>(contexts[i].decisionInput, controls[i].decisionOutput);
		}
		
		statesToConstraints = new ChannelExpression[size];
		
		for (int i = 0; i < size; i++)
		{
			statesToConstraints[i] = new ChannelExpression<>(constraints[i].stateInput, contexts[i].stateOutput);
		}
		
		statesToObjective = new ChannelExpression[size];
		
		for (int i = 0; i < size; i++)
		{
			statesToObjective[i] = new ChannelExpression<>(objective.stateInputs[i], contexts[i].stateOutput);
		}
		
		statesToEquivalence = new ChannelExpression[size];
		
		for (int i = 0; i < size; i++)
		{
			statesToEquivalence[i] = new ChannelExpression<>(equivalence.stateInputs[i], contexts[i].stateOutput);
		}
		
		Port<Double>[] states = new Port[size];
		
		for (int i = 0; i < size; i++)
		{
			states[i] = contexts[i].stateOutput;
		}
		
		statesChart = new Timeline(states);
		
		costChart = new Timeline(objective.costOutput);
	}
	
	// Components
	
	public ContextComponent[] contexts;
	public ControlComponent[] controls;
	public ConstraintComponent[] constraints;
	public ObjectiveComponent objective;
	public EquivalenceComponent equivalence;
	
	// Channels
	
	public ChannelExpression<Boolean>[] decisions;
	public ChannelExpression<Double>[] statesToConstraints;
	public ChannelExpression<Double>[] statesToObjective;
	public ChannelExpression<Double>[] statesToEquivalence;
	
	// Charts

	public Chart statesChart;
	public Chart costChart;

}
