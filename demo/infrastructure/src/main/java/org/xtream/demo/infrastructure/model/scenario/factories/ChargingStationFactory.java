package org.xtream.demo.infrastructure.model.scenario.factories;

import java.util.List;
import java.util.Random;

import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.model.power.ChargingStationComponent;

public class ChargingStationFactory {

	private Graph context;
	
	public ChargingStationFactory(Graph context)
	{
		this.context = context;
	}
	
	public ChargingStationComponent generateChargingStation(Edge edge, Double maximumChargeRate) 
	{
		return new ChargingStationComponent(context, edge, maximumChargeRate);
	}
	
	public ChargingStationComponent generateRandomChargingStation (Double maximumChargeRate, int chargingStationCapacity) 
	{
		List<Edge> edges = context.getEdges();
		Edge randomEdge = edges.get(new Random().nextInt(edges.size()));
		Edge newEdge = context.addEdge(new Edge("" + randomEdge.getSource()+randomEdge.getSource()+Math.random()*Math.random(), randomEdge.getSource(), randomEdge.getSource(), "" + chargingStationCapacity, "station"));
		return new ChargingStationComponent(context, newEdge, maximumChargeRate);
	}
	
	public ChargingStationComponent generateRandomChargingStation (Double maximumChargeRate, Double maximumChargeRateRandomness, int chargingStationCapcity) 
	{
		List<Edge> edges = context.getEdges();
		Edge randomEdge = edges.get(new Random().nextInt(edges.size()));
		Edge newEdge = context.addEdge(new Edge("" + randomEdge.getSource()+randomEdge.getSource()+Math.random()*Math.random(), randomEdge.getSource(), randomEdge.getSource(), "" + chargingStationCapcity, "station"));
		
		return new ChargingStationComponent(context, newEdge, maximumChargeRate-(Math.random()*maximumChargeRateRandomness));
	}

}
