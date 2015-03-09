package org.xtream.demo.infrastructure.model.scenario.scenarioE;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.IterativeKMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.NormDistance;

import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Node;
import org.xtream.demo.infrastructure.datatypes.helpers.SimpleClusterEvaluation;
import org.xtream.demo.infrastructure.model.EnergyComponent;
import org.xtream.demo.infrastructure.model.power.ChargingStationComponent;
import org.xtream.demo.infrastructure.model.power.NetComponent;
import org.xtream.demo.infrastructure.model.power.PowerComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;
import org.xtream.demo.infrastructure.model.scenario.factories.BatteryFactory;
import org.xtream.demo.infrastructure.model.scenario.factories.CarFactory;
import org.xtream.demo.infrastructure.model.scenario.factories.ChargingStationFactory;
import org.xtream.demo.infrastructure.model.scenario.factories.SolarPanelFactory;
import org.xtream.demo.infrastructure.model.scenario.factories.profiles.Car;
import org.xtream.demo.infrastructure.model.scene.ExtendedSceneComponent;
import org.xtream.demo.infrastructure.model.scene.SceneComponent;
import org.xtream.demo.infrastructure.model.transportation.CarComponent;
import org.xtream.demo.infrastructure.model.transportation.TransportationComponent;

public class ScenarioE extends Scenario {

	/*
	 * SCENARIO: ScenarioB
	 * 
	 * PURPOSE: Show 10 cars with low energy driving to destinations
	 * 
	 * WEIGHTS: Cars: SOC: 1
	 * 				  Time: 15
	 * 				  Energy: 1
	 * 			Nets: Low: 1.
	 * 				  Med: 1.
	 * RANDOMNESS: chargingStationSelection
	 * 			   chargingStations and their net allocation (based on KMeans)
	 * 			   net allocation of specific producers and consumers of PowerSystem
	 */
	
	public static int numberCars = 9;
	public static int numberBuses = 0;
	
	public static int numberBatteries = 46;
	public static int numberSolarPanels = 12;
	public static int numberChargingStations = 30;
	
	public static double numberLowVoltageNetsPerConsumers = 1./10.;
	public static double numberMediumVoltageNetsPerLowVoltageNet = 1./10.;
	
	public static int capacityLowVoltageNets = 10;
	public static int capacityMediumVoltageNets = 6;
	
	
	@Override
	public TransportationComponent createTransportationComponent(PowerComponent powerComponent) 
	{
		
		HashSet<Edge> chargingStationPositions = new HashSet<Edge>();
		
		for (int i = 0; i < powerComponent.chargingStations.length; i++)
		{
			chargingStationPositions.add(powerComponent.chargingStations[i].position);
		}
		
		CarFactory carFactory = new CarFactory(context, chargingStationPositions);
		
		HashSet<CarComponent> carSet = new HashSet<CarComponent>();
		
		for (int i = 0; i < 3; i++)
		{
			carSet.add(carFactory.generateCar(context.getEdge("OriginA"), context.getEdge("DestinationA"), Car.RANDOMLOWENERGYCAR));		
		}
		
		for (int i = 0; i < 3; i++)
		{
			carSet.add(carFactory.generateCar(context.getEdge("OriginB"), context.getEdge("DestinationB"), Car.RANDOMLOWENERGYCAR));		
		}
		
		for (int i = 0; i < 3; i++)
		{
			carSet.add(carFactory.generateCar(context.getEdge("OriginC"), context.getEdge("DestinationC"), Car.RANDOMLOWENERGYCAR));		
		}
		
		for (int i = 0; i < numberBuses; i++)
		{
			carSet.add(carFactory.generateCar(context.getEdge("OriginB"), context.getEdge("DestinationB"), Car.BUS));				
		}
		
		CarComponent[] cars = carSet.toArray(new CarComponent[carSet.size()]);
		
		return new TransportationComponent(cars, context);
	}

	@Override
	public PowerComponent createPowerComponent() 
	{
		EnergyComponent[] batteries = new EnergyComponent[numberBatteries];
		EnergyComponent[] solarPanels = new EnergyComponent[numberSolarPanels];
		ChargingStationComponent[] chargingStations = new ChargingStationComponent[numberChargingStations];
		
		int numberLowVoltageNets = (int) Math.ceil((double) numberLowVoltageNetsPerConsumers*(numberBatteries+numberSolarPanels+numberChargingStations));
		int numberMediumVoltageNets = (int) Math.ceil((double) numberMediumVoltageNetsPerLowVoltageNet*numberLowVoltageNets);
		
		NetComponent[] lowVoltageNets = new NetComponent[numberLowVoltageNets];
		NetComponent[] mediumVoltageNets = new NetComponent[numberMediumVoltageNets];
		
		BatteryFactory batteryFactory = new BatteryFactory();
		
		for (int i = 0; i < numberBatteries; i++) 
		{
			batteries[i] = batteryFactory.generateRandomBattery(1., 0.01, 1., 0.01, 85., 0.01, 5., 0.01, 0., 0.01, 85., 0.01);		
		}
		
		SolarPanelFactory solarPanelFactory = new SolarPanelFactory();
		
		for (int i = 0; i < numberSolarPanels; i++)
		{
			solarPanels[i] = solarPanelFactory.generateRandomSolarPanel(10., 0.2);
		}
		
		ChargingStationFactory chargingStationFactory = new ChargingStationFactory(context);
		
		for (int i = 0; i < numberChargingStations; i++)
		{
			switch(i)
			{
				case 0: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("OriginA"), 5.); break;
				case 1: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("OriginB"), 5.); break;
				case 2: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("OriginC"), 5.); break;
				case 3: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("DestinationA"), 5.); break;
				case 4: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("DestinationB"), 5.); break;
				case 5: chargingStations[i] = chargingStationFactory.generateChargingStation(context.getEdge("DestinationC"), 5.); break;
				default: chargingStations[i] = chargingStationFactory.generateRandomChargingStation(5., 4); break;
			}
		}
				
		for (int i = 0; i < numberLowVoltageNets; i++) 
		{
			lowVoltageNets[i] = new NetComponent((numberBatteries+numberSolarPanels+numberChargingStations)/numberLowVoltageNets, 0.);
		}
		
		for (int i = 0; i < numberMediumVoltageNets; i++)
		{
			mediumVoltageNets[i] = new NetComponent(numberLowVoltageNets/numberMediumVoltageNets, 0.);
		}
		
		// chargingStation net allocation by KMeans
		
		Dataset data = new DefaultDataset();
				
		for (int i = 0; i < chargingStations.length; i++)
		{
			Node node = context.getNode(chargingStations[i].position.getSource());
			Instance instance = new SparseInstance(3);	
			instance.put(1,(Double)node.getXPos());
			instance.put(2,(Double)node.getYPos());
			instance.put(3,(Double)node.getZPos());
			instance.setClassValue(i);
			data.add(instance);
		}
		
		Clusterer km = new IterativeKMeans(lowVoltageNets.length, lowVoltageNets.length, 1000, new NormDistance(3), new SimpleClusterEvaluation());
		Dataset[] clusters = km.cluster(data);
		HashMap<Integer, Deque<ChargingStationComponent>> clusterMap = new HashMap<Integer, Deque<ChargingStationComponent>>();
		
		for (int i = 0; i < clusters.length; i++)
		{
			Dataset dataset = clusters[i];
			clusterMap.put(i, new ArrayDeque<ChargingStationComponent>());
			
			for (int j = 0; j < dataset.size(); j++)
			{
				Deque<ChargingStationComponent> temp = clusterMap.get(i);
				temp.push(chargingStations[(int)dataset.get(j).classValue()]);
				clusterMap.put(i, temp);
			}
		}
		
		return new PowerComponent(batteries, solarPanels, chargingStations, lowVoltageNets, mediumVoltageNets, clusterMap, Scenario.DURATION, context);
	}
	
	@Override
	public SceneComponent createSceneComponent(TransportationComponent transportationComponent, PowerComponent powerComponent) 
	{
		return new ExtendedSceneComponent(context, transportationComponent, powerComponent);
	}

}
