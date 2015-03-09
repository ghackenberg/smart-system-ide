package org.xtream.demo.infrastructure.model.power;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.EnergyComponent;

public class PowerComponent extends EnergyComponent
{
	// Parameters
	
	public int duration;
	public Graph graph;
	
	// Components
	
	public EnergyComponent[] batteries;
    public EnergyComponent[] solarPanels;
    public ChargingStationComponent[] chargingStations;
    public NetComponent[] lowVoltageNets;
    public NetComponent[] mediumVoltageNets;
	public HashMap<Integer, Deque<ChargingStationComponent>> clusterMap;

	@SuppressWarnings("unchecked")
	public PowerComponent(EnergyComponent[] batteries, EnergyComponent[] solarPanels, ChargingStationComponent[] chargingStations, NetComponent[] lowVoltageNets, NetComponent[] mediumVoltageNets, HashMap<Integer, Deque<ChargingStationComponent>> clusterMap, int duration, Graph graph)
	{
		this.duration = duration;
		this.batteries = batteries;
		this.solarPanels = solarPanels;
		this.chargingStations = chargingStations;
		this.lowVoltageNets = lowVoltageNets;
		this.mediumVoltageNets = mediumVoltageNets;
		this.clusterMap = clusterMap;

		int size = batteries.length+solarPanels.length+chargingStations.length+lowVoltageNets.length+mediumVoltageNets.length;
		int sizeWithoutNets = batteries.length+solarPanels.length+chargingStations.length;
		
		costInputs = new Port[size];
		cost = new ChannelExpression[size];
		
		lowVoltage = new ChannelExpression[sizeWithoutNets];
		mediumVoltage = new ChannelExpression[lowVoltageNets.length];
		
		List<EnergyComponent> concat = new LinkedList<EnergyComponent>();
		
		concat.addAll(Arrays.asList(batteries));
		concat.addAll(Arrays.asList(solarPanels));
		concat.addAll(Arrays.asList(chargingStations));
		concat.addAll(Arrays.asList(lowVoltageNets));
		concat.addAll(Arrays.asList(mediumVoltageNets));
		
		for (int i = 0; i < size; i++)
		{
			costInputs[i] = new Port<Double>();
			cost[i] = new ChannelExpression<>(costInputs[i], concat.get(i).costOutput);
		}
		
		// TODO [Dominik] Implement allocation strategies
		
		concat = concat.subList(0, sizeWithoutNets-chargingStations.length);
		Collections.shuffle(concat);
		Iterator<EnergyComponent> iterator = concat.iterator();

		for (int i = 0; i < mediumVoltage.length; i++)
		{
			for (int j = 0; j < sizeWithoutNets/lowVoltageNets.length; j++)
			{
				EnergyComponent next;
				
				if (clusterMap.get(j) != null && !(clusterMap.get(j).isEmpty()))
				{
					next = clusterMap.get(j).pop();
				}
				else 
				{
					next = iterator.next();
				}
				
				lowVoltage[i] = new ChannelExpression<>(lowVoltageNets[i].balanceInputs[j], next.powerOutput);
			}
		}
		
		for (int i = 0; i < lowVoltageNets.length; i++)
		{
			for (int j = 0; i < lowVoltageNets.length/mediumVoltageNets.length; i++) 
			{
				mediumVoltage[i] = new ChannelExpression<>(mediumVoltageNets[j].balanceInputs[i], lowVoltageNets[i].balanceOutput);
			}
		}
	}
	
	// Ports
	
	// Inputs
	
	public Port<Double>[] costInputs;
	
	// Channels
	public ChannelExpression<Double>[] cost;
	public ChannelExpression<Double>[] lowVoltage;
	public ChannelExpression<Double>[] mediumVoltage;

	
	public Expression<Double> costExpression = new Expression<Double>(costOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < costInputs.length; i++)
			{
				sum += costInputs[i].get(state, timepoint);
			}
			
			return (timepoint == 0 ? 0 : sum);
		}
	};
	
	public Expression<Double> powerOutputExpression = new Expression<Double>(powerOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			double sum = 0;
			
			for (int i = 0; i < mediumVoltage.length; i++)
			{
				sum += mediumVoltage[i].get(state, timepoint);
			}
			
			return sum;
		}
	};
	
	public Expression<Boolean> validExpression = new Expression<Boolean>(validOutput)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			return true;
		}
	};
}
